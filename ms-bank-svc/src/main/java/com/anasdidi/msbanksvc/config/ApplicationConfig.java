package com.anasdidi.msbanksvc.config;

import io.vertx.core.json.JsonObject;

public final class ApplicationConfig {

  private static ApplicationConfig config;
  private JsonObject json;

  private ApplicationConfig(JsonObject json) {
    this.json = json;
  }

  public static final ApplicationConfig instance() {
    if (config == null) {
      throw new RuntimeException("ApplicationConfig not initialized!");
    }
    return config;
  }

  public static final void create(JsonObject json) {
    if (config != null) {
      throw new RuntimeException("ApplicationConfig already initialized!");
    }
    config = new ApplicationConfig(json);
  }

  private JsonObject getServer() {
    return json.getJsonObject("server");
  }

  public String getServerHost() {
    return getServer().getString("host");
  }

  public int getServerPort() {
    return getServer().getInteger("port");
  }

  @Override
  public String toString() {
    return "ApplicationConfig:" + json.encodePrettily();
  }
}
