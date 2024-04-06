package com.anasdidi.msbanksvc;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.http.entity.ContentType;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anasdidi.msbanksvc.common.BaseVerticle;
import com.anasdidi.msbanksvc.common.CommonUtils;
import com.anasdidi.msbanksvc.common.Constants;
import com.anasdidi.msbanksvc.common.Constants.AppError;
import com.anasdidi.msbanksvc.config.ApplicationConfig;
import com.anasdidi.msbanksvc.config.MessageConfig;
import com.anasdidi.msbanksvc.domain.customer.CustomerVerticle;
import com.anasdidi.msbanksvc.exception.BaseException;
import com.anasdidi.msbanksvc.exception.ValidationException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.ext.auth.VertxContextPRNG;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.exception.CommandExecutionException;
import liquibase.resource.ClassLoaderResourceAccessor;

public class MainVerticle extends AbstractVerticle {

  private final static Logger logger = LoggerFactory.getLogger(MainVerticle.class);
  private final List<BaseVerticle> verticleList;

  public MainVerticle() {
    System.setProperty("vertx.logger-delegate-factory-class-name", SLF4JLogDelegateFactory.class.getName());
    DatabindCodec.mapper().registerModule(new JavaTimeModule());
    verticleList = Arrays.asList(new CustomerVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    setupConfig()
        .compose(v -> getApplicationVersion()
            .andThen(version -> vertx.sharedData().getLocalMap(Constants.LocalMap.NAME)
                .put(Constants.LocalMap.KEY_APP_VERSION, version.result())))
        .compose(version -> setupDatabase(version))
        .compose(v -> Future.all(deployVerticleList()))
        .onSuccess(res -> {
          vertx.createHttpServer().requestHandler(getRequestHandler(startPromise)).listen(
              ApplicationConfig.instance().getServerPort(), ApplicationConfig.instance().getServerHost(), http -> {
                if (http.failed()) {
                  startPromise.fail(http.cause());
                }

                logger.info("HTTP server started on {}:{}", ApplicationConfig.instance().getServerHost(),
                    ApplicationConfig.instance().getServerPort());
                startPromise.complete();
              });
        }).onFailure(res -> startPromise.fail(res));
  }

  private Future<String> getApplicationVersion() {
    return vertx.executeBlocking(() -> {
      MavenXpp3Reader reader = new MavenXpp3Reader();
      Model model;

      File file = new File("pom.xml");
      if (file.exists()) {
        logger.info("[getApplicationVersion] Get from file");
        try (FileReader input = new FileReader(file)) {
          model = reader.read(input);
        }
      } else {
        logger.info("[getApplicationVersion] Get from resource");
        try (InputStreamReader input = new InputStreamReader(
            MainVerticle.class.getResourceAsStream("/META-INF/maven/com.anasdidi/msbanksvc/pom.xml"))) {
          model = reader.read(input);
        }
      }

      logger.info("[getApplicationVersion] version={}", model.getVersion());
      return model.getVersion();
    });
  }

  private Future<Void> setupConfig() {
    Future<JsonObject> application = ConfigRetriever.create(vertx, new ConfigRetrieverOptions()
        .addStore(new ConfigStoreOptions()
            .setType("file")
            .setFormat("yaml")
            .setConfig(new JsonObject().put("path", "application.yml"))))
        .getConfig()
        .andThen(json -> logger.info("[setupConfig] Getting application config..."))
        .onSuccess(ApplicationConfig::create)
        .onFailure(e -> logger.error("Fail to get application config!", e));

    Future<JsonObject> message = ConfigRetriever.create(vertx, new ConfigRetrieverOptions()
        .addStore(new ConfigStoreOptions()
            .setType("file")
            .setFormat("yaml")
            .setConfig(new JsonObject().put("path", "message.yml"))))
        .getConfig()
        .andThen(json -> logger.info("[setupConfig] Getting message config..."))
        .onSuccess(MessageConfig::create)
        .onFailure(e -> logger.error("Fail to get message config!", e));

    return Future.all(application, message)
        .onSuccess(a -> logger.info("[setupConfig] Getting all configs...DONE"))
        .compose(v -> Future.succeededFuture(null));
  }

  private Future<Void> setupDatabase(String version) {
    return vertx.executeBlocking(() -> {
      logger.info("[setupDatabase] Running Liquibase...");
      Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
        try {
          logger.info("[setupDatabase] Liquibase rollback tag={}...", version);
          CommandScope rollback = new CommandScope("rollback");
          rollback.addArgumentValue("changelogFile", "/db/changelog/db.changelog-master.yml");
          rollback.addArgumentValue("url", ApplicationConfig.instance().getJdbcUrl());
          rollback.addArgumentValue("username", ApplicationConfig.instance().getDbUser());
          rollback.addArgumentValue("password", ApplicationConfig.instance().getDbPassword());
          rollback.addArgumentValue("tag", version);
          rollback.execute();
        } catch (CommandExecutionException ex) {
          logger.warn("[setupDatabase] Rollback skipped!", ex);
        } finally {
          logger.info("[setupDatabase] Liquibase rollback tag={}...DONE", version);
        }

        logger.info("[setupDatabase] Liquibase update...");
        CommandScope update = new CommandScope("update");
        update.addArgumentValue("changelogFile", "/db/changelog/db.changelog-master.yml");
        update.addArgumentValue("url", ApplicationConfig.instance().getJdbcUrl());
        update.addArgumentValue("username", ApplicationConfig.instance().getDbUser());
        update.addArgumentValue("password", ApplicationConfig.instance().getDbPassword());
        update.execute();
        logger.info("[setupDatabase] Liquibase update...DONE");
      });
      logger.info("[setupDatabase] Running Liquibase...DONE");
      return null;
    });
  }

  private List<Future<String>> deployVerticleList() {
    return verticleList.stream().map(vertx::deployVerticle).toList();
  }

  private Router getRequestHandler(Promise<Void> startPromise) {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.route().handler(ctx -> {
      String traceId = VertxContextPRNG.current(vertx).nextString(6);
      logger.debug("traceId={}", traceId);
      ctx.put(Constants.Context.TRACEID, traceId).next();
    });

    verticleList.stream().filter(BaseVerticle::hasRouter)
        .forEach(a -> {
          try {
            router.route(a.getSubRouterPath()).subRouter(a.getRouter());
          } catch (Exception e) {
            startPromise.fail(e);
          }
        });

    router.route().failureHandler(ctx -> {
      Throwable t = ctx.failure();
      JsonObject body = JsonObject.of("traceId", ctx.get(Constants.Context.TRACEID));
      int statusCode = 400;
      AppError error;
      boolean found = false;

      if (t instanceof ValidationException ex) {
        error = ex.error;
        body.put("code", ex.error.code).put("message", MessageConfig.instance().getErrorMessage(error))
            .put("errorList", ex.errorList);
        found = true;
      } else if (t instanceof BaseException ex) {
        error = ex.error;
        body.put("code", ex.error.code).put("message", MessageConfig.instance().getErrorMessage(error));
        found = true;
      } else {
        error = AppError.INTERNAL_SERVER_ERROR;
        body.put("code", error.code).put("message", MessageConfig.instance().getErrorMessage(error));
        statusCode = 500;
      }

      if (found) {
        logger.debug("[failureHandler] statusCode={}, error={}, body={}", statusCode, error.toString(), body.encode());
      } else {
        logger.error("[failureHandler] Request Failed!", t);
      }
      CommonUtils.initResponse(ctx)
          .putHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType())
          .setStatusCode(statusCode).end(body.encode());
    });
    return router;
  }
}
