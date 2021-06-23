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
public class LikeRequest {
  @JsonProperty("user_id")
  String userId;

  @JsonProperty("timestamp")
  String timestamp;

  @JsonProperty("trip_id")
  String tripId;

  @JsonProperty("trip_item_id")
  String tripItemId;

  @JsonProperty("suggested_item_id")
  String suggestedItemId;
}
