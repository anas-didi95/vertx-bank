package com.anasdidi.msbanksvc.domain.customer.route;

import java.time.Instant;
import java.util.Date;

import org.apache.commons.validator.GenericValidator;

import com.anasdidi.msbanksvc.common.BaseDTO;
import com.anasdidi.msbanksvc.common.BaseRoute;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.SqlConnection;

public class AddCustomer extends BaseRoute {

  @Override
  protected Future<JsonObject> process(RoutingContext ctx, SqlConnection conn) {
    System.out.println("HERE");
    System.out.println(conn.databaseMetadata().fullVersion());
    return conn
        .query(
            "insert into public.t_cust(nm, created_dt, created_by, ver) values ('test', '2024-02-24', 'system', 0)")
        .execute()
        .onComplete(res -> {
          System.out.println("HERE 3");
          System.out.println(res.succeeded());
          System.out.println(res.failed());
        })
        .compose(res -> {
          System.out.println("HERE 2");
          return Future.future(promise -> {
            AddCustomerDTO dto = (AddCustomerDTO) getDTO(ctx);
            promise.complete(JsonObject.mapFrom(dto).put("dateCreated", Instant.now()).put("rowCount", res.rowCount()));
          });
        });
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
  protected BaseDTO getRequestVariable(RoutingContext ctx) {
    return ctx.body().asJsonObject().mapTo(AddCustomerDTO.class);
  }

  static class AddCustomerDTO extends BaseDTO {
    public final String name;
    public final Instant date;

    @JsonCreator
    private AddCustomerDTO(@JsonProperty("name") String name, @JsonProperty("date") Date date) {
      if (GenericValidator.isBlankOrNull(name)) {
        addError("[name] is mandatory field!");
      }

      this.name = name;
      this.date = Instant.ofEpochMilli(date.getTime());
    }
  }
}
