package com.anasdidi.common;

import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public abstract class BaseVerticle extends AbstractVerticle {

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

  protected final void setupRouter(Router router) {
    if (getHandlerList() == null) {
      System.out.println("No handler added!");
      return;
    }
    getHandlerList().stream().forEach(a -> router.route(a.getHttpMethod(), a.getPath()).handler(a::handle));
  }
}
