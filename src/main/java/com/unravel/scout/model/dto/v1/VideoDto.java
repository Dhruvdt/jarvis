package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.unravel.scout.model.entity.v1.ItemVideoMapping;
import com.unravel.scout.model.entity.v1.Video;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDto {
    private UUID id;
    private String caption;
    private String description;
    private JsonNode image;
    private List<VideoTagDto> tags;
    private JsonNode videoUrls;
    private String source;
    private String sourceType;
    private boolean isPrimary;

    public static VideoDto getVideoDto(ItemVideoMapping itemVideo) {
        Video video = itemVideo.getVideo();
        return VideoDto.builder()
                // .id(video.getId())
                .caption(video.getCaption())
                .image(video.getImage())
                .videoUrls(video.getVideoUrls())
                .isPrimary(itemVideo.isPrimary())
                // .description(video.getDescription())
                // .tags(video.getTags().stream().map(VideoTagDto::mapToTagDto).collect(Collectors.toList()))
                // .source(video.getSource())
                // .sourceType(video.getSourceType())
                .build();
    }
}
