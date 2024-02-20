package com.anasdidi.msbanksvc.common;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public abstract class BaseRoute {

  @SuppressWarnings("unused")
  private EventBus eventBus;

  protected abstract JsonObject process(RoutingContext ctx);

  protected abstract HttpMethod getHttpMethod();

  protected abstract String getPath();

  protected abstract boolean hasEventBus();

  protected abstract BaseDTO validate(RoutingContext ctx);

  protected void sendResponse(RoutingContext ctx, JsonObject resBody) {
    ctx.response().end(resBody.encode());
  }

  protected final BaseDTO getDTO(RoutingContext ctx) {
    return ctx.get(Constants.Context.DTO);
  }

  protected final void handle(RoutingContext ctx) {
    JsonObject responseBody = process(ctx);
    ctx.response().end(responseBody.encode());
  }

  protected final void setEventBus(EventBus eventBus) {
    this.eventBus = eventBus;
  }
}
