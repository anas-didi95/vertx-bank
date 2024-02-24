package com.anasdidi.msbanksvc.domain.customer.route;

import java.util.Collections;
import java.util.concurrent.CompletionStage;

import org.apache.commons.validator.GenericValidator;

import com.anasdidi.msbanksvc.common.BaseDTO;
import com.anasdidi.msbanksvc.common.BaseRoute;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.templates.SqlTemplate;
import io.vertx.sqlclient.templates.annotations.ParametersMapped;

public class AddCustomer extends BaseRoute {

  @Override
  protected Future<JsonObject> process(RoutingContext ctx, SqlConnection conn) {
    AddCustomerDTO dto = (AddCustomerDTO) getDTO(ctx);

    CompletionStage<RowSet<Row>> insert = SqlTemplate.forQuery(conn,
        "insert into public.t_cust(nm, created_dt, created_by, ver) values (#{name}, now(), #{createdBy}, #{version}) returning id")
        .mapFrom(AddCustomerDTO.class)
        .execute(dto)
        .toCompletionStage();

    return Future.fromCompletionStage(insert)
        .map(rst -> rst.iterator().next().getUUID(0))
        .compose(id -> SqlTemplate.forQuery(conn, "select * from public.t_cust where id=#{id}")
            .mapTo(Row::toJson)
            .execute(Collections.singletonMap("id", id)))
        .map(rst -> rst.iterator().next());
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

  @DataObject
  @ParametersMapped
  static class AddCustomerDTO extends BaseDTO {
    public final String name;
    public final String createdBy;
    public final Integer version;

    @JsonCreator
    private AddCustomerDTO(@JsonProperty("name") String name) {
      if (GenericValidator.isBlankOrNull(name)) {
        addError("[name] is mandatory field!");
      }

      this.name = name;
      this.createdBy = "SYSTEM";
      this.version = 0;
    }
  }
}
