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
public class CategoryItemDetailDto {
    private Long id;
    private String itemType;
    private String subType;
    private JsonNode images;


    public static CategoryItemDetailDto mapToCategoryItemDetailDtoWithSubType(ItemDetail item) {
        return CategoryItemDetailDto.builder().id(item.getItemSubType().getId())
                .itemType(item.getItemSubType().getItemType())
                .subType(item.getItemSubType().getSubType())
                .images(item.getItemSubType().getImageUrls())
                .build();
    }


}
