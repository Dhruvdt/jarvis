package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageX {

  @JsonProperty("square")
  @Field("square")
  private String square;

  @JsonProperty("fullscreen")
  @Field("fullscreen")
  private String fullscreen;

  @JsonProperty("thumbnail")
  @Field("thumbnail")
  private String thumb;

  @JsonProperty("portrait")
  @Field("portrait")
  private String portrait;
}
