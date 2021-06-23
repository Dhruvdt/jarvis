package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.ItemImageMapping;
import lombok.*;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RelatedUserTripDto {
    private UUID tripId;
    private String name;
    private String displayName;
    private AddressDto address;
    private List<ImageDto> images;
    private JsonNode properties;
    private Boolean isDefault;
    private Boolean hasItem;

    public static RelatedUserTripDto mapToRelatedUserTripDto(ItemDetail trip, Boolean hasItem) {
        return RelatedUserTripDto.builder()
            .tripId(trip.getId())
            .name(trip.getName())
            .displayName(trip.getDisplayName())
            .address(AddressDto.mapToAddressDto(trip.getAddress()))
            .images(trip.getImages() != null ? trip.getImages().stream()
                .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                .map(ImageDto::getImageDto)
                .limit(1)
                .collect(Collectors.toList()) : null)
            .properties(trip.getProperties())
            .isDefault(trip.getIsDefault())
            .hasItem(hasItem)
            .build();
    }
}