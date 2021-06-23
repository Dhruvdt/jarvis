package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unravel.scout.model.entity.v1.*;
import com.unravel.scout.model.enums.ComponentType;
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
public class ComponentDto {
    private UUID componentId;
    private UUID itemId;
    private String componentName;
    private String displayName;
    private String componentType;
    private Integer order;
    private Integer count;
    private List<Object> items;

    public static ComponentDto mapToComponentDto(ItemComponent itemComponent) {
        var builder = ComponentDto.builder();
        Component component = itemComponent.getComponent();
        ItemDetail itemDetail = itemComponent.getItem();
        Integer order = itemComponent.getOrder();
        ItemComponentId id = itemComponent.getId();

        if (component.getComponentType() == ComponentType.card) {
            builder.items(
                    component.getCardHorizontalComponents().stream()
                            .sorted(Comparator.comparingDouble(CardHorizontalComponent::getScore))
                            .map(c -> ItemDetailDto.mapToItemDetailDtoBasicOnly(c.getComponentItem()))
                            .limit(5)
                            .collect(Collectors.toList())
            );
            builder.count(component.getCardHorizontalComponents().size());
        } else if (component.getComponentType() == ComponentType.html) {
            builder.items(
                    component.getHtmlComponents().stream()
                            .map(HtmlComponentDto::getHtmlComponentDto)
                            .collect(Collectors.toList())
            );
        } else { // hvideo
            var itemVideos = itemDetail.getVideos()
                    .stream()
                    .map(VideoDto::getVideoDto)
                    .collect(Collectors.toList());
            builder.items(
                    HVideoComponentDto.getHVideoComponentDto(itemVideos).getVideos()
            );
        }

        builder.componentId(id.getComponentId())
                // .itemId(id.getItemId())
                .componentName(component.getName())
                .componentType(component.getComponentType().toString())
                .displayName(component.getDisplayName())
                .order(order);
        return builder.build();
    }
}
