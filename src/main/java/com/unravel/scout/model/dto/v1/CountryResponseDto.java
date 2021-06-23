package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.unravel.scout.model.entity.v1.CountryItemDestinationMapping;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.ItemImageMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryResponseDto {
    private UUID countryId;
    private String countryName;
    private String itemType;
    private String countryCode;
    private List<ImageDto> images;
    

    public static CountryResponseDto getCountryList(ItemDetail itemDetail){
        return CountryResponseDto.builder()
                .countryId(itemDetail.getId())
                .countryName(itemDetail.getName())
                .itemType(itemDetail.getItemType())
                .countryCode("")
                .images(itemDetail.getImages().stream()
                        .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                        .map(ImageDto::getImageDto)
                        .collect(Collectors.toList()))
                .build();
    }

}
