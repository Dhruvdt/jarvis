package com.unravel.scout.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponseWrapper<T> {

  @JsonProperty("status")
  private int status;

  @JsonProperty("data")
  private T data;

  @JsonProperty("message")
  private String message;
}
