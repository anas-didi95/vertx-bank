package com.anasdidi.common;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public abstract class BaseHandler {

  protected abstract void handle(RoutingContext ctx);

  protected abstract HttpMethod getHttpMethod();

  protected abstract String getPath();

  protected void sendResponse(RoutingContext ctx, JsonObject resBody) {
    ctx.response().end(resBody.encode());
  }
}
