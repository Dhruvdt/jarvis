package com.unravel.scout.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripCreationRequest {

  @JsonProperty("user_id")
  String userId;

  @JsonProperty("questions")
  Map<String, String> questions;

  @JsonProperty("start_date")
  String startDate;

  @JsonProperty("end_date")
  String endDate;

  @JsonProperty("duration_days")
  int durationDays;

  @JsonProperty("destination")
  String destination;

  @JsonProperty(value = "is_jointly", defaultValue = "false")
  boolean isJointly;

  @JsonProperty("trip_id")
  String tripId;

  @JsonProperty("trip_name")
  String tripName;
}
