package com.anasdidi.msbanksvc.domain.customer.handler;

import java.time.Instant;

import com.anasdidi.msbanksvc.common.BaseHandler;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AddCustomer extends BaseHandler {

  @Override
  protected void handle(RoutingContext ctx) {
    JsonObject reqBody = ctx.body().asJsonObject();
    sendResponse(ctx, reqBody.put("dateCreated", Instant.now()));
  }

  @Override
  protected HttpMethod getHttpMethod() {
    return HttpMethod.POST;
  }

  @Override
  protected String getPath() {
    return "/";
  }
}
