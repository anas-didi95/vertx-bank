package com.anasdidi.msbanksvc.config;

import com.anasdidi.msbanksvc.common.Constants.AppError;

import io.vertx.core.json.JsonObject;

public final class MessageProps {

  private static MessageProps config;
  private final JsonObject json;

  private MessageProps(JsonObject json) {
    this.json = json;
  }

  public static final MessageProps instance() {
    if (config == null) {
      throw new RuntimeException(MessageProps.class.getName() + " not initialized!");
    }
    return config;
  }

  public static final void create(JsonObject json) {
    if (config != null) {
      throw new RuntimeException(MessageProps.class.getName() + " already initialized!");
    }
    config = new MessageProps(json);
  }

  public final String getErrorMessage(AppError error) {
    return json.getJsonObject("error").getString(error.code + "");
  }
}
