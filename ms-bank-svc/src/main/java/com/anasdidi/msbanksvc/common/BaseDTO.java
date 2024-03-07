package com.anasdidi.msbanksvc.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseDTO {

  protected List<String> validateErrorList;

  protected BaseDTO() {
    validateErrorList = new ArrayList<>();
  }

  protected final void addError(BiFunction<String, Object, String> validator, String field, String value) {
    String error = validator.apply(field, value);
    if (error != null) {
      validateErrorList.add(error);
    }
  }
}
