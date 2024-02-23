package com.anasdidi.msbanksvc.exception;

import java.util.List;

public class ValidationException extends BaseException {

  public final List<String> errorList;

  public ValidationException(List<String> errorList) {
    this.errorList = errorList;
  }
}
