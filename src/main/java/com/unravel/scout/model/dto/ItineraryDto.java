package com.unravel.scout.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.dto.ItrItemDto;
import com.unravel.scout.model.entity.Permission;
import com.unravel.scout.model.enums.ItrType;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItineraryDto {

  @JsonProperty("id")
  String id;

  @JsonProperty("is_default")
  Boolean isDefault;

  @JsonProperty("city")
  String city;

  @JsonProperty("country")
  String country;

  @JsonProperty("continent")
  String continent;

  @JsonProperty("type")
  ItrType type;

  @JsonProperty("is_open")
  Boolean isOpen;

  @JsonProperty("start_date")
  String startDate;

  @JsonProperty("end_date")
  String endDate;

  @JsonProperty("created_at")
  Timestamp createdAt;

  @JsonProperty("modified_at")
  Timestamp modifiedAt;

  @JsonProperty("permission")
  Permission permission;

  @JsonProperty("itinerary_items")
  List<ItrItemDto> itineraryItems;
}
