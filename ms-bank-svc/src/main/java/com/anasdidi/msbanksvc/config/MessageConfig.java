package com.anasdidi.msbanksvc.config;

import com.anasdidi.msbanksvc.common.Constants.AppError;

import io.vertx.core.json.JsonObject;

public final class MessageConfig {

  private static MessageConfig config;
  private JsonObject json;

  private MessageConfig(JsonObject json) {
    this.json = json;
  }

  public static final MessageConfig instance() {
    if (config == null) {
      throw new RuntimeException(MessageConfig.class.getName() + " not initialized!");
    }
    return config;
  }

  public static final void create(JsonObject json) {
    if (config != null) {
      throw new RuntimeException(MessageConfig.class.getName() + " already initialized!");
    }
    config = new MessageConfig(json);
  }

  // --- ERROR ---
  private JsonObject getError() {
    return json.getJsonObject("error");
  }

  public String getErrorMessage(AppError error) {
    return getError().getString(error.code + "");
  }
}
