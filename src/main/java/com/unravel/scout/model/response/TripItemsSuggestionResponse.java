package com.unravel.scout.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.entity.ItineraryItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripItemsSuggestionResponse {
  @JsonProperty("status")
  private int status;

  @JsonProperty("data")
  private List<ItineraryItem> data;

  @JsonProperty("message")
  private String message;
}
