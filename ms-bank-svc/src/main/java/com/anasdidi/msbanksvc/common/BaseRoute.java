package com.anasdidi.msbanksvc.common;

import com.anasdidi.msbanksvc.exception.ValidationException;

import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public abstract class BaseRoute {

  protected EventBus eventBus;

  protected abstract JsonObject process(RoutingContext ctx);

  protected abstract HttpMethod getHttpMethod();

  protected abstract String getPath();

  protected abstract BaseDTO getRequestVariable(RoutingContext ctx) throws ValidationException;

  protected final BaseDTO getDTO(RoutingContext ctx) {
    return ctx.get(Constants.Context.DTO);
  }

  protected final void handle(RoutingContext ctx) {
    JsonObject responseBody = process(ctx);
    ctx.response().end(responseBody.encode());
  }

  protected final void validate(RoutingContext ctx) {
    BaseDTO dto = getRequestVariable(ctx);
    if (!dto.validateErrorList.isEmpty()) {
      ctx.fail(new ValidationException(dto.validateErrorList));
    } else {
      ctx.put(Constants.Context.DTO, dto).next();
    }
  }

  protected final void setEventBus(EventBus eventBus) {
    this.eventBus = eventBus;
  }
}
