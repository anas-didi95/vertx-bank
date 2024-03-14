package com.anasdidi.msbanksvc.common;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseDTO {

  protected List<String> validateErrorList;

  protected BaseDTO() {
    validateErrorList = new ArrayList<>();
  }

  protected final void addError(IValidator validator, String field, String value) {
    String error = validator.validate(field, value);
    if (error != null) {
      validateErrorList.add(error);
    }
  }
}
