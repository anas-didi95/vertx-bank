package com.anasdidi.msbanksvc.common;

public final class Constants {

  public final static String DB_SCHEMA = "msbanksvc";

  public final class LocalMap {
    public final static String NAME = "msbanksvc";
    public final static String KEY_APP_VERSION = "LOCALMAP:APP_VER";
  }

  public final class Context {
    public final static String DTO = "CONTEXT:DTO";
    public final static String TRACEID = "CONTEXT:TRACEID";
  }

  public enum AppError {
    VALIDATE_ERROR(40001),
    REQUEST_BODY_EMPTY(40002),
    INTERNAL_SERVER_ERROR(500);

    public final Integer code;

    private AppError(int code) {
      this.code = code;
    }
  }
}
