package com.unravel.scout.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.entity.*;
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
public class TripDto {

  @JsonProperty("is_jointly")
  boolean isJointly;

  @JsonProperty("trip_days")
  List<TripDay> tripDays;

  @JsonProperty("item_id")
  private String id;

  @JsonProperty("display_name")
  private String displayName;

  @JsonProperty("address")
  private Address address;

  @JsonProperty("start_date")
  private String startDate;

  @JsonProperty("end_date")
  private String endDate;

  @JsonProperty("destination_id")
  private List<String> destinationId;

  @JsonProperty("description")
  private String description;

  @JsonProperty("item_type")
  private String itemType;

  @JsonProperty("type")
  private String type;

  @JsonProperty("price")
  private Price price;

  @JsonProperty("image")
  private Image image;

  @JsonProperty("images")
  private List<Image> images;

  @JsonProperty("user_input")
  private Map<String, String> userInput;

  @JsonProperty("questionResponses")
  private QuestionResponses questionResponses;

  @JsonProperty("pace")
  private String pace;

  @JsonProperty("permission")
  private Permission permission;

  @JsonProperty("is_default")
  private boolean isDefault;

  @JsonProperty("share_url")
  private String shareUrl;

  @JsonProperty("vendor_id")
  private String vendorId;

  @JsonProperty("duration_days")
  private int durationDays;

  @JsonProperty("status")
  private String status;

  @JsonProperty("tags")
  private List<Tag> tags;

  @JsonProperty("created_at")
  private String createdAt;

  @JsonProperty("modified_at")
  private String modifiedAt;
}
