package com.anasdidi.msbanksvc.exception;

import com.anasdidi.msbanksvc.common.Constants.AppError;

public class RequestBodyEmptyException extends BaseException {

  public RequestBodyEmptyException() {
    super(AppError.REQUEST_BODY_EMPTY);
  }
}
