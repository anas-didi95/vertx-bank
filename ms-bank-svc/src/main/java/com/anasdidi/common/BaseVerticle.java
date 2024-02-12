package com.anasdidi.common;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public abstract class BaseVerticle extends AbstractVerticle {

  public abstract Router getRouter();

  protected abstract String getBaseURI();

  public final String getSubRouterPath() throws Exception {
    if (getBaseURI() == null || getBaseURI().lastIndexOf("/*") != getBaseURI().length() - 2) {
      throw new Exception("Incorrect Sub Router Path! " + getBaseURI());
    }
    return getBaseURI();
  }

  public final boolean isRouterHandler() {
    return getRouter() != null;
  }
}
