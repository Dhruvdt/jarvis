package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unravel.scout.model.entity.v1.CountryItemDestinationMapping;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.ItemImageMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryDestinationDto {
    private UUID countryId;
    private UUID destinationId;
    private UUID defaultTripId;
    private String country_name;
    private String country_code;
    private String destinationName;
    private String itemType;
    private List<ImageDto> images;

    public static CountryDestinationDto mapToCountryDestinationDto(
            CountryItemDestinationMapping countryDestination) {
        return CountryDestinationDto.builder()
                .countryId(countryDestination.getId().getCountryId())
                .destinationId(countryDestination.getId().getDestinationId())
                .defaultTripId(countryDestination.getDefaultTrip() == null ? null :
                                countryDestination.getDefaultTrip().getId())
                .build();
    }

    public static CountryDestinationDto mapToCountryDestinationImageDto(
            CountryItemDestinationMapping countryDestination) {
        return CountryDestinationDto.builder()
                .destinationId(countryDestination.getId().getDestinationId())
                .destinationName(countryDestination.getDestination().getName())
                .itemType(countryDestination.getDestination().getItemType())
                .country_code("")
                .country_name(countryDestination.getCountry().getName())
                .images(countryDestination.getDestination().getImages().stream()
                         .sorted(Comparator.comparingInt(ItemImageMapping::getOrder))
                         .map(ImageDto::getImageDto)
                         .limit(10)
                         .collect(Collectors.toList()))
                .build();
    }
    
}
