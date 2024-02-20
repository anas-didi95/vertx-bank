package com.anasdidi.msbanksvc.domain.customer;

import java.util.Arrays;
import java.util.List;

import com.anasdidi.msbanksvc.common.BaseRoute;
import com.anasdidi.msbanksvc.common.BaseVerticle;
import com.anasdidi.msbanksvc.domain.customer.route.AddCustomer;

public class CustomerVerticle extends BaseVerticle {

  private final List<BaseRoute> routeList;

  public CustomerVerticle() {
    routeList = Arrays.asList(new AddCustomer());
  }

  @Override
  public boolean hasRouter() {
    return true;
  }

  @Override
  protected String getBaseURI() {
    return "/cust/*";
  }

  @Override
  protected List<BaseRoute> getRouteList() {
    return routeList;
  }

  @Override
  protected String getVerticleName() {
    return this.getClass().getSimpleName();
  }
}
