package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unravel.scout.model.entity.v1.Video;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HVideoComponentDto {
    private List<Object> videos;

    public static HVideoComponentDto getHVideoComponentDto(List<VideoDto> videos) {
        List<Object> videoObjects = new ArrayList<>(videos);
        return HVideoComponentDto.builder().videos(videoObjects).build();
    }
}
