package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.unravel.scout.model.entity.v1.Image;
import com.unravel.scout.model.entity.v1.ItemImageMapping;
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
public class ImageDto {
    private UUID id;
    private String extImageId;
    private String caption;
    private String description;
    private String copyrightText;
    private String copyrightLink;
    private String license;
    @JsonProperty("oneX")
    private JsonNode oneX;
    @JsonProperty("twoX")
    private JsonNode twoX;
    @JsonProperty("halfX")
    private JsonNode halfX;
    @JsonProperty("threeX")
    private JsonNode threeX;
    private Integer order;

    public static ImageDto getImageDto(ItemImageMapping itemImage) {
        Image image = itemImage.getImage();
        return ImageDto.builder()
                .oneX(image.getImageUrls().get("oneX"))
                .twoX(image.getImageUrls().get("twoX"))
                .halfX(image.getImageUrls().get("halfX"))
                .threeX(image.getImageUrls().get("threeX"))
                // .order(itemImage.getOrder())
                // .id(image.getId())
                // .caption(image.getCaption())
                // .description(image.getDescription())
                // .extImageId(image.getExtImageId())
                // .copyrightLink(image.getCopyrightLink())
                // .copyrightText(image.getCopyrightText())
                // .license(image.getLicense())
                .build();
    }
}
