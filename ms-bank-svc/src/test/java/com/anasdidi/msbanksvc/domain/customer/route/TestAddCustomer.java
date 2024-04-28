package com.anasdidi.msbanksvc.domain.customer.route;

import java.util.concurrent.CompletionStage;

import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.anasdidi.msbanksvc.MainVerticle;
import com.anasdidi.msbanksvc.config.ApplicationProps;

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
  static void beforeAll(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeedingThenComplete());
  }

  private CompletionStage<HttpClientResponse> getRequest(Vertx vertx, JsonObject body) {
    int port = ApplicationProps.instance().server.port;
    String host = ApplicationProps.instance().server.host;
    return vertx.createHttpClient()
        .request(HttpMethod.POST, port, host, "/cust/")
        .compose(req -> body != null ? req.send(body.encode()) : req.send())
        .toCompletionStage();
  }

  @Test
  void testSuccess(Vertx vertx, VertxTestContext testContext) {
    CompletionStage<HttpClientResponse> request = getRequest(vertx, new JsonObject().put("name", "Anas Juwaidi"));
    Checkpoint checkpoint = testContext.checkpoint(3);

    Future.fromCompletionStage(request)
        .compose(res -> Future.succeededFuture(res.statusCode()))
        .onComplete(testContext.succeeding(statusCode -> testContext.verify(() -> {
          Assertions.assertEquals(201, statusCode);
          checkpoint.flag();
        })));

    Future.fromCompletionStage(request)
        .compose(res -> Future.succeededFuture(res.getHeader("Content-Type")))
        .onComplete(testContext.succeeding(header -> testContext.verify(() -> {
          Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), header);
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

  @Test
  void testValidationError(Vertx vertx, VertxTestContext testContext) {
    CompletionStage<HttpClientResponse> request = getRequest(vertx, new JsonObject());
    Checkpoint checkpoint = testContext.checkpoint(3);

    Future.fromCompletionStage(request)
        .compose(res -> Future.succeededFuture(res.statusCode()))
        .onComplete(testContext.succeeding(statusCode -> testContext.verify(() -> {
          Assertions.assertEquals(400, statusCode);
          checkpoint.flag();
        })));

    Future.fromCompletionStage(request)
        .compose(res -> Future.succeededFuture(res.getHeader("Content-Type")))
        .onComplete(testContext.succeeding(header -> testContext.verify(() -> {
          Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), header);
          checkpoint.flag();
        })));

    Future.fromCompletionStage(request)
        .compose(HttpClientResponse::body)
        .onComplete(testContext.succeeding(buf -> testContext.verify(() -> {
          JsonObject json = new JsonObject(buf);
          Assertions.assertEquals(40001, json.getInteger("code"));
          Assertions.assertEquals("Validation Error!", json.getString("message"));
          Assertions.assertTrue(!json.getJsonArray("errorList").isEmpty());
          checkpoint.flag();
        })));
  }

  @Test
  void testRequestBodyEmptyError(Vertx vertx, VertxTestContext testContext) {
    CompletionStage<HttpClientResponse> request = getRequest(vertx, null);
    Checkpoint checkpoint = testContext.checkpoint(3);

    Future.fromCompletionStage(request)
        .compose(res -> Future.succeededFuture(res.statusCode()))
        .onComplete(testContext.succeeding(statusCode -> testContext.verify(() -> {
          Assertions.assertEquals(400, statusCode);
          checkpoint.flag();
        })));

    Future.fromCompletionStage(request)
        .compose(res -> Future.succeededFuture(res.getHeader("Content-Type")))
        .onComplete(testContext.succeeding(header -> testContext.verify(() -> {
          Assertions.assertEquals(ContentType.APPLICATION_JSON.getMimeType(), header);
          checkpoint.flag();
        })));

    Future.fromCompletionStage(request)
        .compose(HttpClientResponse::body)
        .onComplete(testContext.succeeding(buf -> testContext.verify(() -> {
          JsonObject json = new JsonObject(buf);
          Assertions.assertEquals(40002, json.getInteger("code"));
          Assertions.assertEquals("Request Body is Empty!", json.getString("message"));
          checkpoint.flag();
        })));
  }
}
