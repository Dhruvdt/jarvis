package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripDay {
  @JsonProperty("day")
  private int day;

  @JsonProperty("pace")
  @Field("pace")
  private String pace;

  @JsonProperty("itinerary_items")
  @Field("itinerary_items")
  private List<ItineraryItem> itineraryItems;
}
