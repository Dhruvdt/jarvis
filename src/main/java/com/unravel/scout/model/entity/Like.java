package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Like {

  @JsonProperty("total_count")
  @Field("total_count")
  int totalCount;

  @JsonProperty("likes")
  Map<String, String> likes;

  @JsonProperty("unlikes")
  Map<String, String> unlikes;

  @JsonProperty("dislikes")
  Map<String, String> dislikes;
}
