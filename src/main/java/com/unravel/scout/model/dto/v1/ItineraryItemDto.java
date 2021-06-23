package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unravel.scout.model.entity.v1.ItemImageMapping;
import com.unravel.scout.model.entity.v1.ItineraryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItineraryItemDto extends ItemDetailDto {
    private UUID id;
    private Integer day;
    private String pace;
    private Integer order;
    private String startTime;
    private String endTime;
    private Integer durationMin;
    private Integer transitDurationMin;
    // private ItemDetailDto item;

    public static ItineraryItemDto mapToItineraryItemDto(ItineraryItem itineraryItem) {
        var builder = ItineraryItemDto.builder()
            // .id(itineraryItem.getId())
            .day(itineraryItem.getDay())
            .pace(itineraryItem.getPace())
            .order(itineraryItem.getOrder())
            .startTime(itineraryItem.getStartTime())
            .endTime(itineraryItem.getEndTime())
            .durationMin(itineraryItem.getDurationMin())
            .transitDurationMin(itineraryItem.getTransitDurationMin());
            // ItemDetails attributes
        if (itineraryItem.getItem() != null) {
            builder.item_id(itineraryItem.getItem().getId())
                .displayName(itineraryItem.getItem().getDisplayName())
                .itemType(itineraryItem.getItem().getItemType())
                .name(itineraryItem.getItem().getName())
                .address(AddressDto.mapToAddressDto(itineraryItem.getItem().getAddress()))
                .description(itineraryItem.getItem().getDescription())
                .images(itineraryItem.getItem().getImages().stream()
                    .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                    .map(ImageDto::getImageDto)
                    .collect(Collectors.toList()))
                // .tags(itineraryItem.getItem().getTags().stream().map(ItemTagDto::mapToTagDto).collect(Collectors.toList()))
                .shareUrl(itineraryItem.getItem().getShareUrl())
                .vendorId(itineraryItem.getItem().getVendorId())
                .properties(itineraryItem.getItem().getProperties())
                .location(LocationDto.builder().latitude(itineraryItem.getItem().getLatitude())
                    .longitude(itineraryItem.getItem().getLongitude()).build())
                .isDefault(itineraryItem.getItem().getIsDefault());
        }
        return builder.build();
    }

    public static ItineraryItemDto mapToItineraryItemBasicDto(ItineraryItem itineraryItem) {
        var builder = ItineraryItemDto.builder()
            // .id(itineraryItem.getId())
            .day(itineraryItem.getDay())
            .pace(itineraryItem.getPace())
            .order(itineraryItem.getOrder())
            .startTime(itineraryItem.getStartTime())
            .endTime(itineraryItem.getEndTime())
            .durationMin(itineraryItem.getDurationMin())
            .transitDurationMin(itineraryItem.getTransitDurationMin());
            // ItemDetails attributes
        if (itineraryItem.getItem() != null) {
            builder.item_id(itineraryItem.getItem().getId())
                .displayName(itineraryItem.getItem().getDisplayName())
                .itemType(itineraryItem.getItem().getItemType())
                .name(itineraryItem.getItem().getName())
                .address(AddressDto.mapToAddressDto(itineraryItem.getItem().getAddress()))
                .description(itineraryItem.getItem().getDescription())
                .images(itineraryItem.getItem().getImages().stream()
                    .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                    .map(ImageDto::getImageDto)
                    .collect(Collectors.toList()))
                // .tags(itineraryItem.getItem().getTags().stream().map(ItemTagDto::mapToTagDto).collect(Collectors.toList()))
                .shareUrl(itineraryItem.getItem().getShareUrl())
                .vendorId(itineraryItem.getItem().getVendorId())
                .properties(itineraryItem.getItem().getProperties())
                .location(LocationDto.builder().latitude(itineraryItem.getItem().getLatitude())
                    .longitude(itineraryItem.getItem().getLongitude()).build())
                .isDefault(itineraryItem.getItem().getIsDefault());
        }
        return builder.build();
    }

    public static ItineraryItemDto mapToItineraryItemLocationDto(ItineraryItem itineraryItem) {
        return ItineraryItemDto.builder()
                .item_id(itineraryItem.getItem().getId())
                .day(itineraryItem.getDay())
                .location(LocationDto.builder().latitude(itineraryItem.getItem().getLatitude()).longitude(itineraryItem.getItem().getLongitude()).build())
                .build();
    }

}
