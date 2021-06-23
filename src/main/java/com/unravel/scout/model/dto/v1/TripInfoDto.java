package com.unravel.scout.model.dto.v1;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.unravel.scout.model.entity.v1.Address;
import com.unravel.scout.model.entity.v1.ItemImageMapping;
import com.unravel.scout.model.entity.v1.ItineraryItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripInfoDto {
    Boolean isJointly;
    //  List<TripDay> tripDays;
    UUID id;
    String displayName;
    Address address;
    String startDate;
    String endDate;
    BigDecimal latitude;
    BigDecimal longitude;
    Set<ItemImageMapping> images;
    List<String> destinationId;
    String description;
    String itemType;
    String type;
    String pace;
    boolean isDefault;
    String shareUrl;
    String vendorId;
    int durationDays;
    String status;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;


}
