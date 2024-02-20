package com.anasdidi.msbanksvc.domain.customer;

import java.util.Arrays;
import java.util.List;

import com.anasdidi.msbanksvc.common.BaseRoute;
import com.anasdidi.msbanksvc.common.BaseVerticle;
import com.anasdidi.msbanksvc.domain.customer.route.AddCustomer;

import io.vertx.ext.web.Router;

public class CustomerVerticle extends BaseVerticle {

  private final Router router;
  private final List<BaseRoute> handlerList;

  public CustomerVerticle() {
    router = Router.router(vertx);
    handlerList = Arrays.asList(new AddCustomer());
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
  protected List<BaseRoute> getHandlerList() {
    return handlerList;
  }

  @Override
  protected String getVerticleName() {
    return this.getClass().getSimpleName();
  }
}
