package com.anasdidi.msbanksvc.exception;

import java.util.List;

import com.anasdidi.msbanksvc.common.Constants.AppError;

public class ValidationException extends BaseException {

  public final List<String> errorList;

  public ValidationException(List<String> errorList) {
    super(AppError.VALIDATE_ERROR.code, AppError.VALIDATE_ERROR.message);
    this.errorList = errorList;
  }
}
