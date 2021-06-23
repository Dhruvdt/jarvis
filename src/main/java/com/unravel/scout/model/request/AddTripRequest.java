package com.unravel.scout.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddTripRequest {

  @JsonProperty("user_id")
  String userId;

  @JsonProperty("start_date")
  String startDate;

  @JsonProperty("end_date")
  String endDate;

  @JsonProperty("trip_change")
  TripChangeRequest tripChangeRequest;
}
