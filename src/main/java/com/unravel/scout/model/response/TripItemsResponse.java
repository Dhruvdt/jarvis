package com.unravel.scout.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.entity.ItineraryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripItemsResponse {

  @JsonProperty("status")
  private int status;

  @JsonProperty("data")
  private ItineraryItem data;

  @JsonProperty("message")
  private String message;
}
