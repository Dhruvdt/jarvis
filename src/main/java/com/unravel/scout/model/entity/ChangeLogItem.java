package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.enums.ItrChangeStatus;
import com.unravel.scout.model.enums.TypeOfAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeLogItem {

  @JsonProperty("user_id")
  String userId;

  @JsonProperty("type_of_Action")
  TypeOfAction typeOfAction;

  @JsonProperty("status")
  ItrChangeStatus status;

  @JsonProperty("new_item")
  ItineraryItem newItem;

  @JsonProperty("timestamp")
  String timestamp;
}
