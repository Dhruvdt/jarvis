package com.unravel.scout.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.dto.ChangeLogItemDto;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripChangeRequest {

  @JsonProperty("display_name")
  String displayName;

  @JsonProperty("start_date")
  String startDate;

  @JsonProperty("end_date")
  String endDate;

  @JsonProperty("duration_days")
  int durationDays;

  @JsonProperty("questions")
  Map<String, String> questions;

  @JsonProperty(value = "is_jointly", defaultValue = "false")
  boolean isJointly;

  @JsonProperty("item_id")
  private String tripId;

  @JsonProperty("changes")
  private Map<String, ChangeLogItemDto> changeLogItemDto;
}
