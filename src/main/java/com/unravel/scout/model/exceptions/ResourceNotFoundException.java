package com.unravel.scout.model.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@Builder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  private final String resourceName;

  public ResourceNotFoundException(String resourceName) {
    super(String.format("%s not found", resourceName));
    this.resourceName = resourceName;
  }
}
