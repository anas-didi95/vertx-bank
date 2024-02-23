package com.anasdidi.msbanksvc.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public abstract class BaseVerticle extends AbstractVerticle {

  private final static Logger logger = LoggerFactory.getLogger(BaseVerticle.class);
  private Router router;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    setupRouter()
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

  private final Future<Void> setupRouter() {
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
                .handler(ctx -> ctx.put(Constants.Context.DTO, a.validate(ctx)).next())
                .handler(a::handle));
        logger.info("[setupRouter] {} route added", getVerticleName());
      }
      promise.complete();
    });
  }

  public final Router getRouter() {
    return router;
  }
}
