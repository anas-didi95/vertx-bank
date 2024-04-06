package com.anasdidi.msbanksvc.exception;

import com.anasdidi.msbanksvc.common.Constants.AppError;

public abstract class BaseException extends RuntimeException {

  public final AppError error;

  protected BaseException(AppError error) {
    this.error = error;
  }
}
