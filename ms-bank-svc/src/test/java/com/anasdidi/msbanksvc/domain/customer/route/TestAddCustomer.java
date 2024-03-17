package com.anasdidi.msbanksvc.domain.customer.route;

import java.util.concurrent.CompletionStage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.anasdidi.msbanksvc.MainVerticle;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class TestAddCustomer {

  @BeforeAll
  static void beforeEach(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeedingThenComplete());
  }

  @Test
  void processSuccess(Vertx vertx, VertxTestContext testContext) {
    Checkpoint checkpoint = testContext.checkpoint(2);

    CompletionStage<HttpClientResponse> request = vertx.createHttpClient()
        .request(HttpMethod.POST, 8888, "localhost", "/cust/")
        .compose(req -> req.send(new JsonObject().put("name", "Anas Juwaidi").encode()))
        .toCompletionStage();

    Future.fromCompletionStage(request)
        .compose(res -> Future.succeededFuture(res.statusCode()))
        .onComplete(testContext.succeeding(statusCode -> testContext.verify(() -> {
          Assertions.assertEquals(201, statusCode);
          checkpoint.flag();
        })));

    Future.fromCompletionStage(request)
        .compose(HttpClientResponse::body)
        .onComplete(testContext.succeeding(res -> testContext.verify(() -> {
          JsonObject json = new JsonObject(res);
          Assertions.assertNotNull(json.getString("id"));
          Assertions.assertEquals(0, json.getInteger("version"));
          checkpoint.flag();
        })));
  }
}
