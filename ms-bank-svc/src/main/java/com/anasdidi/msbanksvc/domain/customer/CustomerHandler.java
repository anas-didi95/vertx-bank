package com.anasdidi.msbanksvc.domain.customer;

import io.vertx.ext.web.RoutingContext;

class CustomerHandler {

  void addCustomer(RoutingContext ctx) {
    ctx.response().end("Add Customer");
  }
}
