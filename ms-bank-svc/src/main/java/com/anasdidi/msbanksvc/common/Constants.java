package com.anasdidi.msbanksvc.common;

public final class Constants {

  public final class Context {
    public final static String DTO = "CONTEXT:DTO";
    public final static String TRACEID = "CONTEXT:TRACEID";
  }

  public enum AppError {
    VALIDATE_ERROR(40001, "Validation Error!"),
    REQUEST_BODY_EMPTY(40002, "Request Body is Empty!"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error!");

    public final Integer code;
    public final String message;

    private AppError(int code, String message) {
      this.code = code;
      this.message = message;
    }
  }
}
