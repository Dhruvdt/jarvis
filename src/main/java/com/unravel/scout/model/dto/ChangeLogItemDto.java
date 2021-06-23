package com.unravel.scout.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.enums.ItrChangeStatus;
import com.unravel.scout.model.enums.TypeOfAction;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangeLogItemDto {
  @JsonProperty("id")
  String id;

  @JsonProperty("user_id")
  String userId;

  @JsonProperty("day")
  int day;

  @JsonProperty("order")
  int order;

  @JsonProperty("target_item_id")
  String targetItemId;

  @JsonProperty("type_of_Action")
  TypeOfAction typeOfAction;

  @JsonProperty("status")
  ItrChangeStatus status;

  @JsonProperty("new_item_id")
  String newItemId;

  @JsonProperty("suggested_item_id")
  String suggestedItemId;

  @JsonProperty("timestamp")
  String timestamp;
}
