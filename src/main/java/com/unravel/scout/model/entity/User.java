package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "users")
public class User {

  @Id private String _id;

  @JsonProperty("id")
  @Field("id")
  private String id;

  @JsonProperty("my_itineraries")
  @Field("my_itineraries")
  List<String> myItineraries;

  @JsonProperty("shared_with_me")
  @Field("shared_with_me")
  List<String> sharedWithMe;

  @JsonProperty("suggested")
  @Field("suggested")
  List<String> suggested;

  @JsonProperty("recently_viewed")
  @Field("recently_viewed")
  Map<String, String> recentlyViewed;
}
