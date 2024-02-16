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

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    setupRouter()
        .onSuccess(e -> {
          logger.info("[start] {} started", getVerticleName());
          startPromise.complete();
        })
        .onFailure(e -> startPromise.fail(e));
  }

  public abstract Router getRouter();

  protected abstract String getBaseURI();

  protected abstract List<BaseHandler> getHandlerList();

  protected abstract String getVerticleName();

  public final String getSubRouterPath() throws Exception {
    if (getBaseURI() == null || getBaseURI().lastIndexOf("/*") != getBaseURI().length() - 2) {
      throw new Exception("Incorrect Sub Router Path! " + getBaseURI());
    }
    return getBaseURI();
  }

  public final boolean isRouterHandler() {
    return getRouter() != null;
  }

  private final Future<Void> setupRouter() {
    return Future.future(promise -> {
      if (!isRouterHandler()) {
        logger.info("[setupRouter] {} no router", getVerticleName());
      } else if (getHandlerList() == null) {
        logger.info("[setupRouter] {} no handler added", getVerticleName());
      } else {
        getHandlerList().stream()
            .peek(a -> logger.info("[setupRouter] {} httpMethod={}, path={}", getVerticleName(), a.getHttpMethod(),
                a.getPath()))
            .forEach(a -> getRouter().route(a.getHttpMethod(), a.getPath())
                .handler(ctx -> {
                  ctx.put(Constants.Context.DTO, a.validate(ctx));
                  ctx.next();
                })
                .handler(a::handle));
        logger.info("[setupRouter] {} handler added", getVerticleName());
      }
      promise.complete();
    });
  }
}
