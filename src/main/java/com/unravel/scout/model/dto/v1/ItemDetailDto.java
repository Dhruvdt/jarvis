package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.ItemImageMapping;
import com.unravel.scout.model.entity.v1.ItemSubType;
import com.unravel.scout.model.entity.v1.ItineraryItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDetailDto {
    private UUID item_id;

    private String displayName;

    private String itemType;

    private ItemSubType itemSubType;

    private String name;

    private AddressDto address;

    private String description;

    private List<ImageDto> images;

    private List<VideoDto> videos;

    private List<ItemTagDto> tags;

    private String shareUrl;

    private String vendorId;

    private JsonNode properties;

    private List<ComponentDto> components;

    private LocationDto location;

    private Boolean isDefault;

    private List<UUID> destinationItems;

    private List<UUID> itemDestinations;

    private List<CountryDestinationDto> countryDestinations;

    private List<TripDayDto> tripDays;

    public static ItemDetailDto mapToItemDetailDto(ItemDetail item) {
        var itemBuilder = ItemDetailDto.builder();
            itemBuilder.item_id(item.getId())
                .displayName(item.getDisplayName())
                .itemType(item.getItemType())
                // .itemSubType(item.getItemSubType())
                .name(item.getName())
                .address(AddressDto.mapToAddressDto(item.getAddress()))
                .description(item.getDescription())
                .images(item.getImages() != null ? item.getImages().stream()
                        .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                        .map(ImageDto::getImageDto)
                        .limit(10)
                        .collect(Collectors.toList()) : null)
                // .videos(item.getVideos().stream()
                //         .map(VideoDto::getVideoDto)
                //         .collect(Collectors.toList()))
                // .tags(item.getTags().stream().map(ItemTagDto::mapToTagDto).collect(Collectors.toList()))
                .shareUrl(item.getShareUrl())
                .vendorId(item.getVendorId())
                .properties(PropertiesDto.getPropertiesDto(item.getProperties()))
                .components(item.getComponents().stream()   // TODO: limit to 5 components with count?
                        .map(ComponentDto::mapToComponentDto)
                        .collect(Collectors.toList()))
                .location(LocationDto.builder()
                    .latitude(item.getLatitude())
                    .longitude(item.getLongitude()).build())
                // .tripDays(new ArrayList<>(item.getTripDays()))
                // .countryDestinations(item.getCountryDestinations().stream()
                //         .map(CountryDestinationDto::mapToCountryDestinationDto)
                //         .collect(Collectors.toList()))
                // .itemDestinations(item.getItemDestinations().stream()
                //         .map(ItemDetail::getId)
                //         .collect(Collectors.toList()))
                // .destinationItems(item.getDestinationItems().stream()
                //         .map(ItemDetail::getId)
                //         .collect(Collectors.toList()));
                .isDefault(item.getIsDefault());

        return itemBuilder.build();
    }

    /**
     * Maps only basic attributes avoiding nested ones like components
     * @param item
     * @return ItemDetailDto
     */
    public static ItemDetailDto mapToItemDetailDtoBasicOnly(ItemDetail item) {
        return ItemDetailDto.builder().item_id(item.getId())
                .displayName(item.getDisplayName())
                .itemType(item.getItemType())
                .name(item.getName())
                .address(AddressDto.mapToAddressDto(item.getAddress()))
                .images(item.getImages() !=null  ? item.getImages().stream()
                        .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                        .map(ImageDto::getImageDto)
                        .limit(1)
                        .collect(Collectors.toList()) : null)
                .shareUrl(item.getShareUrl())
                .vendorId(item.getVendorId())
                .properties(item.getProperties())
                .location(LocationDto.builder().latitude(item.getLatitude())
                                               .longitude(item.getLongitude()).build())
                .isDefault(item.getIsDefault())
                .build();
    }

    public static ItemDetailDto mapToItemDetailDtoWithItineraryItem(ItemDetail item) {
        return ItemDetailDto.builder().item_id(item.getId())
                .displayName(item.getDisplayName())
                .itemType(item.getItemType())
                .name(item.getName())
                .address(AddressDto.mapToAddressDto(item.getAddress()))
                .images(item.getTripDays().stream()
                        .flatMap(i -> i.getItem().getImages().stream())
                        .map(ImageDto::getImageDto)
                        .limit(1)
                        .collect(Collectors.toList()))
                .shareUrl(item.getShareUrl())
                .vendorId(item.getVendorId())
                .properties(item.getProperties())
                .location(LocationDto.builder().latitude(item.getLatitude())
                        .longitude(item.getLongitude()).build())
                .isDefault(item.getIsDefault())
                .build();
    }

    public static ItemDetailDto mapToItemDetailCountryDestinationDto(ItemDetail item) {
        var itemBuilder = ItemDetailDto.builder();
        itemBuilder.countryDestinations(item.getCountryDestinations().stream()
                .map(CountryDestinationDto::mapToCountryDestinationImageDto)
                .collect(Collectors.toList()));

        return itemBuilder.build();
    }

    public static ItemDetailDto mapToItemDetailDtoBasic(ItemDetail item) {
        return ItemDetailDto.builder().item_id(item.getId())
                .displayName(item.getDisplayName())
                .itemType(item.getItemType())
                .name(item.getName())
                .address(AddressDto.mapToAddressDto(item.getAddress()))
                .description(item.getDescription())
                .images(item.getImages() !=null  ? item.getImages().stream()
                        .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                        .map(ImageDto::getImageDto)
                        .limit(1)
                        .collect(Collectors.toList()) : null)
                .location(LocationDto.builder().latitude(item.getLatitude())
                        .longitude(item.getLongitude()).build())
                .isDefault(item.getIsDefault())
                .build();
    }

    //find items - subtype
    public static ItemDetailDto mapToItemDetailDtoSubType(ItemDetail item) {
        return ItemDetailDto.builder().item_id(item.getId())
                .displayName(item.getDisplayName())
                .itemType(item.getItemType())
                .name(item.getName())
                .description(item.getDescription())
                .address(AddressDto.mapToAddressDto(item.getAddress()))
                .images(item.getImages() !=null  ? item.getImages().stream()
                        .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                        .map(ImageDto::getImageDto)
                        .limit(1)
                        .collect(Collectors.toList()) : null)
                .shareUrl(item.getShareUrl())
                .vendorId(item.getVendorId())
                .properties(item.getProperties())
                .location(LocationDto.builder().latitude(item.getLatitude())
                        .longitude(item.getLongitude()).build())
                .isDefault(item.getIsDefault())
                .build();
    }
}
