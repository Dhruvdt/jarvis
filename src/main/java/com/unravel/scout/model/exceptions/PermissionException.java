package com.unravel.scout.model.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@Builder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PermissionException extends RuntimeException {

  private final String message;

  public PermissionException(String message) {
    super(message);
    this.message = message;
  }
}
