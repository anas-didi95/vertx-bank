package com.anasdidi.msbanksvc.domain.customer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class CustomerVerticle extends AbstractVerticle {

  private final Router router;

  public CustomerVerticle() {
    router = Router.router(vertx);
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    router.route(HttpMethod.GET, "/cust")
        .handler(ctx -> ctx.response().end("Customer Handler up"));
    System.out.println(this.getClass().getCanonicalName() + " deployed successfully");
    startPromise.complete();
  }

  public Router getRouter() {
    return router;
  }
}
