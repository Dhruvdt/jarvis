package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.enums.Permissions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "trips")
public class Trip {

  @JsonProperty("is_jointly")
  @Field("is_jointly")
  boolean isJointly;

  @JsonProperty("trip_days")
  @Field("trip_days")
  List<TripDay> tripDays;

  @Id private String _id;

  @JsonProperty("item_id")
  @Field("item_id")
  private String id;

  @JsonProperty("display_name")
  @Field("display_name")
  private String displayName;

  @JsonProperty("address")
  private Address address;

  @JsonProperty("start_date")
  @Field("start_date")
  private String startDate;

  @JsonProperty("end_date")
  @Field("end_date")
  private String endDate;

  @JsonProperty("destination_id")
  @Field("destination_id")
  private List<String> destinationId;

  @JsonProperty("description")
  @Field("description")
  private String description;

  @JsonProperty("item_type")
  @Field("item_type")
  private String itemType;

  @JsonProperty("type")
  @Field("type")
  private String type;

  @JsonProperty("price")
  @Field("price")
  private Price price;

  @JsonProperty("image")
  @Field("image")
  private Image image;

  @JsonProperty("images")
  @Field("images")
  private List<Image> images;

  @JsonProperty("user_input")
  @Field("user_input")
  private Map<String, String> userInput;

  @JsonProperty("pace")
  @Field("pace")
  private String pace;

  @JsonProperty("permission")
  @Field("permission")
  private Permission permission;

  @JsonProperty("is_default")
  @Field("is_default")
  private boolean isDefault;

  @JsonProperty("share_url")
  @Field("share_url")
  private String shareUrl;

  @JsonProperty("vendor_id")
  @Field("vendor_id")
  private String vendorId;

  @JsonProperty("duration_days")
  @Field("duration_days")
  private int durationDays;

  @JsonProperty("status")
  @Field("status")
  private String status;

  @JsonProperty("tags")
  @Field("tags")
  private List<Tag> tags;

  @JsonProperty("created_at")
  @Field("created_at")
  private String createdAt;

  @JsonProperty("modified_at")
  @Field("modified_at")
  private String modifiedAt;

  public Boolean isOwner(String userId) {
    return this.getPermission().getOwner().equals(userId);
  }

  public Boolean canSuggest(String userId) {
    return (this.getPermission().getSharedWIth().containsKey(userId))
        ? this.getPermission().getSharedWIth().get(userId).equals(Permissions.SUGGEST)
        : this.getPermission().getOwner().equals(userId);
  }

  public Permissions findUserPermissions(String userId) {
    if (this.getPermission().getSharedWIth().containsKey(userId)) {
      return this.getPermission().getSharedWIth().get(userId);
    } else if (this.isOwner(userId)) {
      return Permissions.OWNER;
    }
    return null;
  }
}
