package com.anasdidi.msbanksvc.domain.customer.handler;

import java.time.Instant;

import com.anasdidi.msbanksvc.common.BaseDTO;
import com.anasdidi.msbanksvc.common.BaseHandler;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AddCustomer extends BaseHandler {

  @Override
  protected void handle(RoutingContext ctx) {
    AddCustomerDTO dto = (AddCustomerDTO) getDTO(ctx);
    sendResponse(ctx, new JsonObject().put("dateCreated", Instant.now()).put("newName", dto.name + "_new"));
  }

  @Override
  protected HttpMethod getHttpMethod() {
    return HttpMethod.POST;
  }

  @Override
  protected String getPath() {
    return "/";
  }

  @Override
  protected BaseDTO validate(RoutingContext ctx) {
    JsonObject body = ctx.body().asJsonObject();
    return new AddCustomerDTO(body.getString("name"));
  }

  class AddCustomerDTO extends BaseDTO {
    final String name;

    public AddCustomerDTO(String name) {
      if (name == null) {
        throw new RuntimeException("Name empty");
      }
      this.name = name;
    }
  }
}
