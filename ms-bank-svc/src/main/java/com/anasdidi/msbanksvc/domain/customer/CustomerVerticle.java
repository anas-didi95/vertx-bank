package com.anasdidi.msbanksvc.domain.customer;

import com.anasdidi.common.BaseVerticle;

import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;

public class CustomerVerticle extends BaseVerticle {

  private final Router router;
  private final CustomerHandler handler;

  public CustomerVerticle() {
    router = Router.router(vertx);
    handler = new CustomerHandler();
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    router.route(HttpMethod.GET, "/").handler(handler::addCustomer);
    System.out.println(this.getClass().getCanonicalName() + " deployed successfully");
    startPromise.complete();
  }

  @Override
  public Router getRouter() {
    return router;
  }

  @Override
  protected String getBaseURI() {
    return "/cust/*";
  }
}
