package com.unravel.scout.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareTripRequest {
  @JsonProperty("owner_id")
  private String ownerId;

  @JsonProperty("trip_id")
  private String tripId;

  @JsonProperty("share_with")
  private List<String> shareWith;
}
