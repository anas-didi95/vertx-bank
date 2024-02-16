package com.anasdidi.msbanksvc.domain.customer.handler;

import java.time.Instant;

import org.apache.commons.validator.GenericValidator;

import com.anasdidi.msbanksvc.common.BaseDTO;
import com.anasdidi.msbanksvc.common.BaseHandler;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AddCustomer extends BaseHandler {

  @Override
  protected void handle(RoutingContext ctx) {
    AddCustomerDTO dto = (AddCustomerDTO) getDTO(ctx);
    System.out.println(JsonObject.mapFrom(dto).encode());
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
    return ctx.body().asJsonObject().mapTo(AddCustomerDTO.class);
  }

  static class AddCustomerDTO extends BaseDTO {
    public final String name;

    @JsonCreator
    private AddCustomerDTO(@JsonProperty("name") String name) {
      if (GenericValidator.isBlankOrNull(name)) {
        throw new RuntimeException("Name empty");
      }
      this.name = name;
    }
  }
}
