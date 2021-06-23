package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unravel.scout.model.entity.v1.HtmlComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HtmlComponentDto {
    private UUID id;
    private String htmlText;

    public static HtmlComponentDto getHtmlComponentDto(HtmlComponent htmlComponent) {
        return HtmlComponentDto.builder()
                .id(htmlComponent.getId())
                .htmlText(htmlComponent.getHtmlText())
                .build();
    }
}
