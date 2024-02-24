package com.anasdidi.msbanksvc.common;

import com.anasdidi.msbanksvc.exception.ValidationException;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;

public abstract class BaseRoute {

  protected EventBus eventBus;

  protected abstract Future<JsonObject> process(RoutingContext ctx, SqlConnection conn);

  protected abstract HttpMethod getHttpMethod();

  protected abstract String getPath();

  protected abstract BaseDTO getRequestVariable(RoutingContext ctx) throws ValidationException;

  protected final BaseDTO getDTO(RoutingContext ctx) {
    return ctx.get(Constants.Context.DTO);
  }

  protected final void handle(RoutingContext ctx, Pool pool) {
    pool.withTransaction(conn -> process(ctx, conn))
        .onSuccess(body -> ctx.response().end(body.encode()))
        .onFailure(ctx::fail);
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
