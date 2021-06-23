package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unravel.scout.model.entity.v1.CardHorizontalComponent;
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
public class CardHorizontalComponentDto {
    private UUID id;
    private ItemDetailDto componentItem;
    private Double score;

    public static CardHorizontalComponentDto getCardHorizontalComponentDto(CardHorizontalComponent component) {
        return CardHorizontalComponentDto.builder()
                .id(component.getId())
                .score(component.getScore())
                .componentItem(component.getComponentItem() == null ? null :
                        ItemDetailDto.mapToItemDetailDtoBasicOnly(component.getComponentItem()))
                .build();
    }
}
