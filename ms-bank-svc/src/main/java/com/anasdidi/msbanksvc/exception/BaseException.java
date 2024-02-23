package com.anasdidi.msbanksvc.exception;

import com.anasdidi.msbanksvc.common.Constants.AppError;

public abstract class BaseException extends RuntimeException {

  public final int code;
  public final String message;

  protected BaseException(AppError error) {
    this.code = error.code;
    this.message = error.message;
  }
}
