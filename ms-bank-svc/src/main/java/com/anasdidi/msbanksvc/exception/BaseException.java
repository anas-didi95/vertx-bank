package com.anasdidi.msbanksvc.exception;

public abstract class BaseException extends RuntimeException {

  public final int code;
  public final String message;

  protected BaseException(int code, String message) {
    this.code = code;
    this.message = message;
  }
}
