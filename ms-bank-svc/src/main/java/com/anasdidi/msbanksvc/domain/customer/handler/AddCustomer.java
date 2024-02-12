package com.anasdidi.msbanksvc.domain.customer.handler;

import com.anasdidi.common.BaseHandler;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AddCustomer extends BaseHandler {

  @Override
  protected void handle(RoutingContext ctx) {
    sendResponse(ctx, new JsonObject().put("hello", "world").put("thank", "you"));
  }

  @Override
  protected HttpMethod getHttpMethod() {
    return HttpMethod.GET;
  }

  @Override
  protected String getPath() {
    return "/";
  }
}
