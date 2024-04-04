package com.anasdidi.msbanksvc.config;

import java.util.Map;

import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;

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

  // --- SERVER ---
  private JsonObject getServer() {
    return json.getJsonObject("server");
  }

  public String getServerHost() {
    return getServer().getString("host");
  }

  public int getServerPort() {
    return getServer().getInteger("port");
  }

  // --- Db ---
  private JsonObject getDb() {
    return json.getJsonObject("db");
  }

  private String getDbHost() {
    return getDb().getString("host");
  }

  private Integer getDbPort() {
    return getDb().getInteger("port");
  }

  private String getDbDatabase() {
    return getDb().getString("database");
  }

  public String getDbUser() {
    return getDb().getString("user");
  }

  public String getDbPassword() {
    return getDb().getString("password");
  }

  public PgConnectOptions getPgConnectOptions() {
    return new PgConnectOptions()
        .setHost(getDbHost())
        .setPort(getDbPort())
        .setDatabase(getDbDatabase())
        .setUser(getDbUser())
        .setPassword(getDbPassword())
        .setProperties(Map.of("search_path", getDb().getString("schema")));
  }

  public String getJdbcUrl() {
    return String.format("jdbc:postgresql://%s:%d/%s", getDbHost(), getDbPort(), getDbDatabase());
  }

  @Override
  public String toString() {
    return "ApplicationConfig:" + json.encodePrettily();
  }
}
