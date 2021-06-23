package com.unravel.scout.controller;

import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.QuestionDto;
import com.unravel.scout.model.entity.v1.Question;
import com.unravel.scout.model.response.RestResponseWrapper;
import com.unravel.scout.repository.QuestionRepository;
import com.unravel.scout.service.QuestionsService;
import com.unravel.scout.utils.EndpointRoutesV1;
import com.unravel.scout.utils.annotataions.ApiController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@ApiController
@RequestMapping(EndpointRoutesV1.ROOT_ENDPOINT)
@Api(
    value = EndpointRoutesV1.ROOT_ENDPOINT,
    tags = {"Questions for Trip and jointly Creation"})
public class QuestionsController {

  private final QuestionRepository questionRepository;
  private final QuestionsService questionService;
  private final ModelMapper modelMapper;

  @Autowired
  public QuestionsController(
          final QuestionRepository questionRepository, QuestionsService questionService, final ModelMapper modelMapper) {
    this.questionRepository = questionRepository;
    this.questionService = questionService;
    this.modelMapper = modelMapper;
  }
  @Deprecated
  @GetMapping(EndpointRoutesV1.QUESTIONS)
  @ApiOperation(value = "Get Itinerary")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_FETCHED,
      response = QuestionDto.class,
      responseContainer = "List")
  public ResponseEntity<?> getQuestions(
      @RequestParam(value = "is_jointly", required = false, defaultValue = "false")
          Boolean isJointly) {
    log.info(String.format("GET %s", EndpointRoutesV1.QUESTIONS));
    List<com.unravel.scout.model.entity.Question> questions = questionRepository.findAllByIsJointly(isJointly);
    RestResponseWrapper<List<QuestionDto>> response =
        new RestResponseWrapper<>(
            1,
            questions.stream()
                .map(q -> modelMapper.map(q, QuestionDto.class))
                .collect(Collectors.toList()),
            "Successful");
    log.info("SUCCESS");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping(EndpointRoutesV1.QUESTIONS_LIST)
  @ApiOperation(value = "Get Itinerary")
  @ApiResponse(
          code = 200,
          message = ApiResponseMessages.ITEM_FETCHED,
          response = QuestionDto.class,
          responseContainer = "List")
  public ResponseEntity<?> getQuestionsList(@RequestParam(value = "is_jointly") Boolean isJointly) {
    log.info(String.format("GET %s", EndpointRoutesV1.QUESTIONS_LIST));
    List<Question> questions = questionService.getQuestionsDocument(isJointly);
    log.info("SUCCESS");
    return new ResponseEntity(
            new RestResponseWrapper<>(1, questions, ApiResponseMessages.ITEM_FETCHED),
            HttpStatus.OK);
  }
}
