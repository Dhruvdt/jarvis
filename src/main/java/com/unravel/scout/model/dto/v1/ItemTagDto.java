package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unravel.scout.model.entity.v1.ItemTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemTagDto {
    private String name;
    private String displayName;
    private Double score;

    public static ItemTagDto mapToTagDto(ItemTag tag) {
        return ItemTagDto.builder()
                .name(tag.getName())
                .displayName(tag.getDisplayName())
                .score(tag.getScore())
                .build();
    }
}
