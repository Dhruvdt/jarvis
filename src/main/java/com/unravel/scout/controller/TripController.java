package com.unravel.scout.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.TripDto;
import com.unravel.scout.model.dto.v1.CountryResponseDto;
import com.unravel.scout.model.dto.v1.ItemDetailDto;
import com.unravel.scout.model.dto.v1.RenameTripRequest;
import com.unravel.scout.model.dto.v1.TripInformationsDto;
import com.unravel.scout.model.entity.Trip;
import com.unravel.scout.model.request.TripChangeRequest;
import com.unravel.scout.model.request.TripCreationRequest;
import com.unravel.scout.model.request.v1.CreateTripDto;
import com.unravel.scout.model.response.RestResponseWrapper;
import com.unravel.scout.service.TripCreationService;
import com.unravel.scout.service.TripService;
import com.unravel.scout.utils.EndpointRoutesV1;
import com.unravel.scout.utils.annotataions.ApiController;
import com.unravel.universal.UnravelResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@ApiController
@RequestMapping(EndpointRoutesV1.ROOT_ENDPOINT)
@Api(
    value = EndpointRoutesV1.ROOT_ENDPOINT,
    tags = {"Trip and jointly"})
public class TripController {

  private final TripService tripService;
  private final TripCreationService tripCreationService;
  private final ObjectMapper objectMapper;

  @Autowired
  public TripController(
      final TripService tripService, final TripCreationService tripCreationService) {
    this.tripService = tripService;
    this.tripCreationService = tripCreationService;
    this.objectMapper = new ObjectMapper();
  }

  @Deprecated
  @GetMapping(EndpointRoutesV1._ID)
  @ApiOperation(value = "Get Trip or Jointly by ID")
  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = Trip.class)
  public ResponseEntity<TripDto> getTripById(@PathVariable String id) {
    log.info(String.format("GET %s => id: %s", EndpointRoutesV1._ID, id));
    TripDto trip = tripService.getTripDto(id);
    log.info("SUCCESS");
    return new ResponseEntity(
        new RestResponseWrapper<>(1, trip, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);
  }

  @Deprecated
  @PostMapping(EndpointRoutesV1.CREATE_TRIP)
  @ApiOperation(value = "Create a trip or Jointly")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_CREATED,
      response = String.class,
      responseContainer = "RestResponseWrapper")
  public ResponseEntity<?> createTrip(@RequestBody TripCreationRequest tripCreationRequest) {
    log.info(String.format("GET %s ", EndpointRoutesV1.CREATE_TRIP));
    Optional<TripDto> tripDto = tripCreationService.createUserTrip(tripCreationRequest);
    if (tripDto.isPresent()) {
      log.info("SUCCESS");
      return new ResponseEntity(
          new RestResponseWrapper<>(1, tripDto, ApiResponseMessages.ITEM_CREATED), HttpStatus.OK);
    } else {
      log.info("Failed");
      return new ResponseEntity(
          new RestResponseWrapper<>(0, null, ApiResponseMessages.OPERATION_FAILED), HttpStatus.OK);
    }
  }

  @Deprecated
  @PostMapping(EndpointRoutesV1.UPDATE_TRIP)
  @ApiOperation(value = "Update a trip or Jointly")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_UPDATED,
      response = String.class,
      responseContainer = "RestResponseWrapper")
  public ResponseEntity<?> updateTrip(@RequestBody TripChangeRequest tripChangeRequest) {
    Trip trip = tripService.update(tripChangeRequest);
    if (trip == null) {
      return new ResponseEntity(
          new RestResponseWrapper<>(0, null, "Cant update a Unravel Trip"), HttpStatus.OK);
    } else {
      return new ResponseEntity(
          new RestResponseWrapper<>(
              1, tripService.update(tripChangeRequest), ApiResponseMessages.ITEM_UPDATED),
          HttpStatus.OK);
    }
  }

  @Deprecated
  @DeleteMapping(EndpointRoutesV1.DELETE_TRIP)
  @ApiOperation(value = "Update a trip or Jointly")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_UPDATED,
      response = String.class,
      responseContainer = "RestResponseWrapper")
  public ResponseEntity<?> deleteTrip(@PathVariable String tripId) {

    try {
      tripService.deleteTrip(tripId);
      return new ResponseEntity<>(
          new RestResponseWrapper<>(1, "Deleted", ApiResponseMessages.ITEM_UPDATED), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          new RestResponseWrapper<>(
              0, e.getLocalizedMessage(), ApiResponseMessages.OPERATION_FAILED),
          HttpStatus.OK);
    }
  }

  @PostMapping(EndpointRoutesV1.CREATE_NEW_TRIP)
  @ApiOperation(value = "Create a new trip")
  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = UnravelResponse.class)
  public ResponseEntity<UnravelResponse> createNewTrip(@RequestBody CreateTripDto createTripDto) throws JsonProcessingException {
    log.info(String.format("GET %s ", EndpointRoutesV1.CREATE_NEW_TRIP));
    UnravelResponse response = tripCreationService.processingUserTrip(createTripDto);

    log.debug("Response: {}", objectMapper.writeValueAsString(response));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping(EndpointRoutesV1.DELETE_NEW_TRIP)
  @ApiOperation(value = "Delete a trip or Jointly")
  @ApiResponse(code = 200,
      message = ApiResponseMessages.ITEM_DELETE,
      response = String.class,
      responseContainer = "RestResponseWrapper")
  public ResponseEntity<?> deleteNewTrip(@PathVariable("tripId") @NotNull String tripId) {
    log.info(String.format("DELETE %s ", EndpointRoutesV1.DELETE_NEW_TRIP));
    Boolean isDeleted = tripCreationService.deleteTrip(tripId);
    if (isDeleted) {
      log.info("SUCCESS");
      return new ResponseEntity<>(
          new RestResponseWrapper<>(1, "Deleted", ApiResponseMessages.ITEM_DELETE), HttpStatus.OK);
    } else {
      log.info("Failed");
      return new ResponseEntity<>(new RestResponseWrapper<>(0, "Deletion Failed",
              ApiResponseMessages.OPERATION_FAILED), HttpStatus.OK);
    }
  }


  @GetMapping(EndpointRoutesV1.FETCH_TRIP)
  @ApiOperation(value = "Get Trip or Jointly by ID")
  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = Trip.class)
  public ResponseEntity<TripDto> fetchTripById(@RequestParam("userId") String userId,@RequestParam int count) {
    log.info(String.format("GET %s => id: %s", EndpointRoutesV1.FETCH_TRIP, userId));
    List<ItemDetailDto> trip = tripService.fetchTripDto(userId,count);
    log.info("SUCCESS");
    return new ResponseEntity(
            new RestResponseWrapper<>(1, trip, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);
  }

  @GetMapping(EndpointRoutesV1.FETCH_RECOMMENDED_TRIP_BY_USERID)
  @ApiOperation(value = "Get Recommendation")
  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = Trip.class)
  public ResponseEntity<TripInformationsDto> fetchTripByUserId(@RequestParam("userId") String userId,
                                                               @RequestParam("count") int count) {
    log.info(String.format("GET %s => id: %s", EndpointRoutesV1.FETCH_RECOMMENDED_TRIP_BY_USERID, userId));

    List<ItemDetailDto> trip = tripService.fetchUserRecommendedTrip(userId, count);

    log.info("SUCCESS");
    return new ResponseEntity(
        new RestResponseWrapper<>(1, trip, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);
  }


  @GetMapping(EndpointRoutesV1.FETCH_TRIP_COUNTRY)
  @ApiOperation(value = "fetch Countries")
  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = Trip.class)
  public ResponseEntity<TripInformationsDto> fetchTripCountry(@RequestParam("userId") Optional<String> userId,@RequestParam("count") Optional<Integer> count) {
    log.info(String.format("GET %s => id: %s", EndpointRoutesV1.FETCH_TRIP_COUNTRY, userId));

    if(!userId.isPresent() || userId == null || userId.equals("")) {
   	 log.info("Failed");
   	return new ResponseEntity(new RestResponseWrapper<>(0, null, ApiResponseMessages.USER_ID_NULL), HttpStatus.OK);
   }
   else if (!count.isPresent() || count==null || count.equals("")){
   	 log.info("Failed");
   	return new ResponseEntity(new RestResponseWrapper<>(0, null, ApiResponseMessages.COUNT_NULL), HttpStatus.OK);
   }
   else
   {
   	 List<CountryResponseDto> trip = tripService.fetchCountryByItemType(userId.get(), count.get());
		 log.info("SUCCESS");
		 return new ResponseEntity(new RestResponseWrapper<>(1, trip, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);
   }
  }

  @GetMapping(EndpointRoutesV1.FETCH_TRIP_CATEGORY)
  @ApiOperation(value = "fetch categories")
  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = Trip.class)
  public ResponseEntity<UnravelResponse> fetchCategory(@RequestParam("trip_id")  Optional<String>  userId, @RequestParam(defaultValue = "0") Optional<Integer> page,
                                                              @RequestParam(defaultValue = "3") Optional<Integer> size) {
    log.info(String.format("GET %s => id: %s", EndpointRoutesV1.FETCH_TRIP_CATEGORY, userId));
    UnravelResponse response = tripService.fetchCategoryByTripId(userId,page,size);
    log.info("SUCCESS");
    return new ResponseEntity<>(response, HttpStatus.OK);

  }

  @GetMapping(EndpointRoutesV1.FETCH_ITEM_BY_TRIP_SUBTYPE)
  @ApiOperation(value = "fetch Items by Its SubType Id")
  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = Trip.class)
  public ResponseEntity<UnravelResponse> fetchItemsBySubType(@RequestParam("sub_type_id") Optional<String> subTypeId, @RequestParam("page") Optional<Integer> page,
                                                                 @RequestParam("size") Optional<Integer> size) {
    log.info(String.format("GET %s => id: %s", EndpointRoutesV1.FETCH_ITEM_BY_TRIP_SUBTYPE, subTypeId));
    UnravelResponse response = tripService.fetchItemsBySubType(subTypeId,size);
    log.info("SUCCESS");
    return new ResponseEntity<>(response, HttpStatus.OK);

  }

  @PostMapping(EndpointRoutesV1.RENAME_TRIP)
  @ApiOperation(value = "Rename Trip")
  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_UPDATED, response = UnravelResponse.class)
  public ResponseEntity<UnravelResponse> renameTrip(@RequestBody RenameTripRequest renameTripRequest)
      throws JsonProcessingException {
    log.info("Request to rename trip: {}", objectMapper.writeValueAsString(renameTripRequest));
    UnravelResponse response = tripService.renameTrip(renameTripRequest);
    log.debug("Response: {}", objectMapper.writeValueAsString(response));
    // return new ResponseEntity<>(response, HttpStatus.OK);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

}
