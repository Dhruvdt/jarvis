package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionResponses {

  @JsonProperty("end_date")
  private String endDate;

  @JsonProperty("destination_id")
  private String destinationId;

  @JsonProperty("questions")
  private Map<String, String> questions;

  @JsonProperty("duration_days")
  private int durationDays;

  @JsonProperty("start_date")
  private String startDate;
}
