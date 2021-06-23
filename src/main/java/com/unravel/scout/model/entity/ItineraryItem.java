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
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItineraryItem {

  @JsonProperty("item_id")
  @Field("item_id")
  private String itemId;

  @JsonProperty("day")
  @Field("day")
  private int day;

  @JsonProperty("order")
  private int order;

  @JsonProperty("display_name")
  @Field("display_name")
  private String displayName;

  @JsonProperty("item_type")
  @Field("item_type")
  private String itemType;

  @JsonProperty("image")
  private Image image;

  @JsonProperty("destination_id")
  @Field("destination_id")
  private String destinationId;

  @JsonProperty("solo_pref")
  @Field("solo_pref")
  private int soloPref;

  @JsonProperty("duration_min")
  @Field("duration_min")
  private int durationMin;

  @JsonProperty("senior_pref")
  @Field("senior_pref")
  private int seniorPref;

  @JsonProperty("start_time")
  @Field("start_time")
  private String startTime;

  @JsonProperty("end_time")
  @Field("end_time")
  private String endTime;

  @JsonProperty("mapping_type")
  @Field("mapping_type")
  private String mappingType;

  @JsonProperty("couple_pref")
  @Field("couple_pref")
  private int couplePref;

  @JsonProperty("transit_duration_min")
  @Field("transit_duration_min")
  private int transitDurationMin;

  @JsonProperty("transit_distance")
  @Field("transit_distance")
  private double transitDistance;

  @JsonProperty("price")
  @Field("price")
  private Price price;

  @JsonProperty("address")
  private Address address;

  @JsonProperty("location")
  private Geocode location;

  @JsonProperty("attractions")
  private List<String> attractions;

  @JsonProperty("family_pref")
  @Field("family_pref")
  private int familyPref;

  @JsonProperty("likes")
  private Like likes;

  @JsonProperty("suggestions")
  private Map<String, ChangeLogItem> suggestions;

  public ItineraryItem swap(ItineraryItem item) {
    this.itemId = item.getItemId();
    this.day = item.getDay();
    this.order = item.getOrder();
    this.displayName = item.getDisplayName();
    this.itemType = item.getItemType();
    this.image = item.getImage();
    this.destinationId = item.getDestinationId();
    this.soloPref = item.getSoloPref();
    this.durationMin = item.getDurationMin();
    this.seniorPref = item.getSeniorPref();
    this.startTime = item.getStartTime();
    this.mappingType = item.getMappingType();
    this.couplePref = item.getCouplePref();
    this.transitDurationMin = item.getTransitDurationMin();
    this.address = item.getAddress();
    this.location = item.getLocation();
    this.attractions = item.getAttractions();
    this.familyPref = item.getFamilyPref();
    this.likes = item.getLikes();
    this.suggestions = item.getSuggestions();
    return this;
  }
}
