package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image {

  @JsonProperty("oneX")
  private ImageX oneX;

  @JsonProperty("twoX")
  private ImageX twoX;

  @JsonProperty("threeX")
  private ImageX threeX;

  @JsonProperty("halfX")
  private ImageX halfX;

}
