package com.anasdidi.msbanksvc;

import com.anasdidi.msbanksvc.domain.customer.CustomerVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    CustomerVerticle customerVerticle = new CustomerVerticle();

    vertx.deployVerticle(customerVerticle).onSuccess(res -> {
      Router router = Router.router(vertx);
      router.route().subRouter(customerVerticle.getRouter());

      vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
        if (http.failed()) {
          startPromise.fail(http.cause());
        }

        System.out.println("HTTP server started on port 8888");
        startPromise.complete();
      });
    }).onFailure(res -> startPromise.fail(res));
  }
}
