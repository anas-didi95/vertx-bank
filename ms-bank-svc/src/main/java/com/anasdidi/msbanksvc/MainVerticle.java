package com.anasdidi.msbanksvc;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anasdidi.msbanksvc.common.BaseVerticle;
import com.anasdidi.msbanksvc.domain.customer.CustomerVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

  private final static Logger logger = LoggerFactory.getLogger(MainVerticle.class);
  private final List<BaseVerticle> verticleList;

  public MainVerticle() {
    System.setProperty("vertx.logger-delegate-factory-class-name", SLF4JLogDelegateFactory.class.getName());
    verticleList = Arrays.asList(new CustomerVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Future.all(deployVerticleList()).onSuccess(res -> {
      vertx.createHttpServer().requestHandler(getRequestHandler(startPromise)).listen(8888, http -> {
        if (http.failed()) {
          startPromise.fail(http.cause());
        }

        logger.info("HTTP server started on port 8888");
        startPromise.complete();
      });
    }).onFailure(res -> startPromise.fail(res));
  }

  private List<Future<String>> deployVerticleList() {
    return verticleList.stream().map(vertx::deployVerticle).toList();
  }

  private Router getRequestHandler(Promise<Void> startPromise) {
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    verticleList.stream().filter(BaseVerticle::isRouterHandler)
        .forEach(a -> {
          try {
            router.route(a.getSubRouterPath()).subRouter(a.getRouter());
          } catch (Exception e) {
            startPromise.fail(e);
          }
        });
    return router;
  }
}
