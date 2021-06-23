package com.unravel.scout.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unravel.scout.model.enums.QuestionType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionDto {
  @JsonProperty("question_id")
  String questionId;

  @JsonProperty("question_txt")
  String questionText;

  @JsonProperty("options")
  List<String> options;

  @JsonProperty("order")
  int order;

  @JsonProperty("question_type")
  QuestionType questionType;
}
