package com.unravel.scout.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.entity.ChangeLogItem;
import com.unravel.scout.model.entity.Geocode;
import com.unravel.scout.model.entity.Like;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItrItemDto {
  @JsonProperty("id")
  private String id;

  @JsonProperty("suggestions")
  private Map<String, ChangeLogItem> suggestions;

  @JsonProperty("activity_name")
  private String activityName;

  @JsonProperty("image_url")
  private String imageUrl;

  @JsonProperty("short_desc")
  private String shortDesc;

  @JsonProperty("street_address")
  private String streetAddress;

  @JsonProperty("type")
  private String type;

  @JsonProperty("order")
  private int order;

  @JsonProperty("day")
  private int day;

  @JsonProperty("time")
  private String time;

  @JsonProperty("geo_code")
  private Geocode geocode;

  @JsonProperty("continent")
  private String continent;

  @JsonProperty("country")
  private String country;

  @JsonProperty("city")
  private String city;

  @JsonProperty("likes")
  private Like likes;

  @JsonProperty("attractions")
  private List<String> attractions;

  @JsonProperty("venue")
  private String venue;

  @JsonProperty("option_no")
  private int optionNo;

  @JsonProperty("is_matched")
  private boolean isMatched;
}
