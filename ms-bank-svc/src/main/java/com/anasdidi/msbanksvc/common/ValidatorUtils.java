package com.anasdidi.msbanksvc.common;

import org.apache.commons.validator.GenericValidator;

public final class ValidatorUtils {

  public final static IValidator isMandatoryField() {
    return (field, value) -> {
      boolean isError = false;
      if (value instanceof String v) {
        isError = GenericValidator.isBlankOrNull(v);
      } else {
        isError = value == null;
      }
      return getErrorMessage(isError, field);
    };
  }

  private final static String getErrorMessage(boolean isError, String field) {
    if (!isError) {
      return null;
    }
    String message = "[%s] is mandatory field!";
    return String.format(message, field);
  }
}
