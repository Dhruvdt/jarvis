package com.unravel.scout.model.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@Builder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotValidException extends RuntimeException {

  private final String resourceName;

  public NotValidException(String resourceName) {
    super(String.format("%s :: invalid Data", resourceName));
    this.resourceName = resourceName;
  }
}
