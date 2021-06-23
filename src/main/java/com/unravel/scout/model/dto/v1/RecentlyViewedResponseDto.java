package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.istack.NotNull;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.ItemImageMapping;
import com.unravel.scout.model.entity.v1.RecentlyViewed;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentlyViewedResponseDto {
    private Long id;
    private String userId;
    private UUID item_id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name ;
    private String description;
    private String itemType;
    private String shareUrl;
    private List<ImageDto> images;

    private String displayName;
    private AddressDto address;
    private String vendorId;
    private JsonNode properties;
    private LocationDto location;
    private Boolean isDefault;



    public static RecentlyViewedResponseDto getRecentlyViewed(RecentlyViewed recentlyViewed) {
        return RecentlyViewedResponseDto.builder()
               /* .id(recentlyViewed.getId())
                .userId(recentlyViewed.getUserId())*/
                .item_id(recentlyViewed.getItem().getId())
                .displayName(recentlyViewed.getItem().getDisplayName())
                .itemType(recentlyViewed.getItem().getItemType())
                .name(recentlyViewed.getItem().getName())
                .address(AddressDto.mapToAddressDto(recentlyViewed.getItem().getAddress()))
                .images(recentlyViewed.getItem().getImages().stream()
                        .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                        .map(ImageDto::getImageDto)
                        .limit(1)
                        .collect(Collectors.toList()))
                .shareUrl(recentlyViewed.getItem().getShareUrl())
                .vendorId(recentlyViewed.getItem().getVendorId())
                .properties(recentlyViewed.getItem().getProperties())
                .location(LocationDto.builder().latitude(recentlyViewed.getItem().getLatitude())
                        .longitude(recentlyViewed.getItem().getLongitude()).build())
                .isDefault(recentlyViewed.getItem().getIsDefault())
                .build();
    }



}
