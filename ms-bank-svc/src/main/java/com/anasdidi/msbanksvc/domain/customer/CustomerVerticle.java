package com.anasdidi.msbanksvc.domain.customer;

import java.util.Arrays;
import java.util.List;

import com.anasdidi.common.BaseHandler;
import com.anasdidi.common.BaseVerticle;
import com.anasdidi.msbanksvc.domain.customer.handler.AddCustomer;

import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class CustomerVerticle extends BaseVerticle {

  private final Router router;
  private final List<BaseHandler> handlerList;

  public CustomerVerticle() {
    router = Router.router(vertx);
    handlerList = Arrays.asList(new AddCustomer());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    setupRouter(router);
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

  @Override
  protected List<BaseHandler> getHandlerList() {
    return handlerList;
  }
}
