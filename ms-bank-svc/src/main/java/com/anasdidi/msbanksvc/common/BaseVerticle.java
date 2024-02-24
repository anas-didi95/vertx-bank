package com.anasdidi.msbanksvc.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public abstract class BaseVerticle extends AbstractVerticle {

  private final static Logger logger = LoggerFactory.getLogger(BaseVerticle.class);
  private Router router;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Pool pool = setupDatabase();

    setupRouter(pool)
        .onSuccess(e -> {
          logger.info("[start] {} started", getVerticleName());
          startPromise.complete();
        })
        .onFailure(e -> startPromise.fail(e));
  }

  public abstract boolean hasRouter();

  protected abstract String getBaseURI();

  protected abstract List<BaseRoute> getRouteList();

  protected abstract String getVerticleName();

  public final String getSubRouterPath() throws Exception {
    if (getBaseURI() == null || getBaseURI().lastIndexOf("/*") != getBaseURI().length() - 2) {
      throw new Exception("Incorrect Sub Router Path! " + getBaseURI());
    }
    return getBaseURI();
  }

  public final Router getRouter() {
    return router;
  }

  private final Future<Void> setupRouter(Pool pool) {
    if (hasRouter() && router == null) {
      router = Router.router(vertx);
    }

    return Future.future(promise -> {
      if (getRouteList() == null) {
        logger.info("[setupRouter] {} no route added", getVerticleName());
      } else {
        getRouteList().stream()
            .peek(a -> logger.info("[setupRouter] {} httpMethod={}, path={}", getVerticleName(), a.getHttpMethod(),
                a.getPath()))
            .peek(a -> a.setEventBus(vertx.eventBus()))
            .forEach(a -> router.route(a.getHttpMethod(), a.getPath())
                .handler(a::validate)
                .handler(ctx -> a.handle(ctx, pool)));
        logger.info("[setupRouter] {} route added", getVerticleName());
      }
      promise.complete();
    });
  }

  private Pool setupDatabase() {
    PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(5432)
        .setHost("postgres")
        .setDatabase("postgres")
        .setUser("postgres")
        .setPassword("postgres");
    PoolOptions poolOptions = new PoolOptions();
    return PgBuilder
        .pool()
        .with(poolOptions)
        .connectingTo(connectOptions)
        .using(vertx)
        .build();
  }
}
