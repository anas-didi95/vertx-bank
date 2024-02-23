package com.anasdidi.msbanksvc.domain.customer.route;

import java.time.Instant;
import java.util.Date;

import org.apache.commons.validator.GenericValidator;

import com.anasdidi.msbanksvc.common.BaseDTO;
import com.anasdidi.msbanksvc.common.BaseRoute;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AddCustomer extends BaseRoute {

  @Override
  protected JsonObject process(RoutingContext ctx) {
    AddCustomerDTO dto = (AddCustomerDTO) getDTO(ctx);
    return JsonObject.mapFrom(dto).put("dateCreated", Instant.now());
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
    public final Instant date;

    @JsonCreator
    private AddCustomerDTO(@JsonProperty("name") String name, @JsonProperty("date") Date date) {
      if (GenericValidator.isBlankOrNull(name)) {
        throw new RuntimeException("Name empty");
      }
      this.name = name;
      this.date = Instant.ofEpochMilli(date.getTime());
    }
  }

}
