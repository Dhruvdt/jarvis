package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.unravel.scout.model.entity.v1.Address;
import com.unravel.scout.model.entity.v1.ItemSubType;
import com.unravel.scout.model.entity.v1.ItemTag;
import com.unravel.scout.model.entity.v1.ItineraryItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
public class TripInformationsDto {
    private UUID id;
    private String displayName;
    private String itemType;
    private ItemSubType itemSubType;
    private String name;
    private Address address;
    private String description;
    private Set<ItemTag> tags;
    private String shareUrl;
    private String vendorId;
    private JsonNode properties;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isDefault;
    private Set<ItineraryItem> tripDays;
}
