package com.anasdidi.msbanksvc.config;

import java.util.Map;

import com.anasdidi.msbanksvc.common.Constants;

import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;

public final class ApplicationProps {

  private static ApplicationProps config;
  public final Server server;
  public final Db db;

  private ApplicationProps(JsonObject json) {
    this.server = new Server(json.getJsonObject("server"));
    this.db = new Db(json.getJsonObject("db"));
  }

  public static final ApplicationProps instance() {
    if (config == null) {
      throw new RuntimeException(ApplicationProps.class.getSimpleName() + " not initialized!");
    }
    return config;
  }

  public static final void create(JsonObject json) {
    if (config != null) {
      throw new RuntimeException(ApplicationProps.class.getSimpleName() + " already initialized!");
    }
    config = new ApplicationProps(json);
  }

  public final class Server {
    public final String host;
    public final int port;

    private Server(JsonObject json) {
      this.host = json.getString("host");
      this.port = json.getInteger("port");
    }
  }

  public final class Db {
    public final String host;
    public final int port;
    public final String database;
    public final String user;
    public final String password;
    public final PgConnectOptions pgConnectOptions;
    public final String jdbcUrl;

    private Db(JsonObject json) {
      this.host = json.getString("host");
      this.port = json.getInteger("port");
      this.database = json.getString("database");
      this.user = json.getString("user");
      this.password = json.getString("password");
      this.pgConnectOptions = new PgConnectOptions()
          .setHost(host)
          .setPort(port)
          .setDatabase(database)
          .setUser(user)
          .setPassword(password)
          .setProperties(Map.of("search_path", Constants.DB_SCHEMA));
      this.jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
    }
  }
}
