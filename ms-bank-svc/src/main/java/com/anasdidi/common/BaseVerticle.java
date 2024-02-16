package com.anasdidi.common;

import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public abstract class BaseVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    setupRouter()
        .onSuccess(e -> {
          System.out.println("Verticle started");
          startPromise.complete();
        })
        .onFailure(e -> startPromise.fail(e));
  }

  public abstract Router getRouter();

  protected abstract String getBaseURI();

  protected abstract List<BaseHandler> getHandlerList();

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
      if (getHandlerList() == null) {
        System.out.println("No handler added!");
      } else {
        getHandlerList().stream().forEach(a -> getRouter().route(a.getHttpMethod(), a.getPath()).handler(a::handle));
        System.out.println("[setupRouter] Completed");
      }
      promise.complete();
    });
  }
}
