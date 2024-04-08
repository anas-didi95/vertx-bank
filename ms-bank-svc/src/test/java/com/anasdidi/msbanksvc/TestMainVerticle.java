package com.anasdidi.msbanksvc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.anasdidi.msbanksvc.common.Constants;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeAll
  static void beforeAll(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeedingThenComplete());
  }

  @Test
  void testVerticleDeployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    Assertions
        .assertNotNull(vertx.sharedData().getLocalMap(Constants.LocalMap.NAME).get(Constants.LocalMap.KEY_APP_VERSION));
    testContext.completeNow();
  }
}
