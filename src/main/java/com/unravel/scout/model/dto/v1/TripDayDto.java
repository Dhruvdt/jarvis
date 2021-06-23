package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripDayDto {
    private Integer day;
    private String pace;
    private List<ItineraryItemDto> itineraryItems;

    public static TripDayDto getTripDayDto(Integer day, List<ItineraryItemDto> itineraryItems) {
        return TripDayDto.builder()
            .day(day)
            .pace(itineraryItems == null || itineraryItems.isEmpty() ? null :
                    itineraryItems.get(0).getPace())    // TODO: calculate pace
            .itineraryItems(itineraryItems)             // TODO: order items in a day?
            .build();
    }
}
