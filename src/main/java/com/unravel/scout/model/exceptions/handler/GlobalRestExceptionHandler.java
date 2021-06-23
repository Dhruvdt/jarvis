package com.unravel.scout.model.exceptions.handler;

import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.exceptions.ResourceNotFoundException;
import com.unravel.scout.model.response.RestResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleExceptions(
      ResourceNotFoundException exception, WebRequest webRequest) {
    RestResponseWrapper<String> response =
        new RestResponseWrapper<String>(
            0, exception.getLocalizedMessage(), ApiResponseMessages.ITEM_NOT_FOUND);
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }
}
