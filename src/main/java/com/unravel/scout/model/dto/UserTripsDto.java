package com.unravel.scout.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTripsDto {

  // toDO: Itinerary should be changed to ItineraryDto
  @JsonProperty("my_trips")
  List<TripDto> myTrips;
  // toDO: Itinerary should be changed to ItineraryDto
  @JsonProperty("shared_with_me")
  List<TripDto> sharedWithMe;

  @JsonProperty("suggested")
  List<TripDto> suggested;

  @JsonProperty("recently_viewed")
  List<TripDto> recentlyViewed;

  @JsonProperty("user_id")
  private String userId;
}
