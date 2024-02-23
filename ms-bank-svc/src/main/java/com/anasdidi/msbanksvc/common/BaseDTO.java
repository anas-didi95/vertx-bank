package com.anasdidi.msbanksvc.common;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDTO {

  protected List<String> validateErrorList;

  protected BaseDTO() {
    validateErrorList = new ArrayList<>();
  }

  protected final void addError(String error) {
    validateErrorList.add(error);
  }
}
