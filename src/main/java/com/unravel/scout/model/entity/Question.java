package com.unravel.scout.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "questions")
public class Question {

  @Id String _id;

  @JsonProperty("question_id")
  @Field("question_id")
  String questionId;

  @JsonProperty("question_txt")
  @Field("question_txt")
  String questionText;

  @JsonProperty("options")
  @Field("options")
  List<String> options;

  @JsonProperty("order")
  @Field("order")
  int order;

  @JsonProperty("question_type")
  @Field("question_type")
  QuestionType questionType;

  @JsonProperty("is_jointly")
  @Field("is_jointly")
  boolean isJointly;
}
