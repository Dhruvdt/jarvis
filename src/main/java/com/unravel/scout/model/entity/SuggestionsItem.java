package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class SuggestionsItem {

  @JsonProperty("type_of_Action")
  private String typeOfAction;

  @JsonProperty("new_item_id")
  private String newItem_id;

  @JsonProperty("user_id")
  private String userId;

  @JsonProperty("status")
  private String status;

  @JsonProperty("timestamp")
  private String timestamp;
}
