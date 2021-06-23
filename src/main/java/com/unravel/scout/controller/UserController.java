package com.unravel.scout.controller;

import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.TripDto;
import com.unravel.scout.model.dto.UserTripsDto;
import com.unravel.scout.model.entity.User;
import com.unravel.scout.model.exceptions.ResourceNotFoundException;
import com.unravel.scout.model.request.LikeRequest;
import com.unravel.scout.model.request.ShareTripRequest;
import com.unravel.scout.model.response.RestResponseWrapper;
import com.unravel.scout.service.TripService;
import com.unravel.scout.service.UserService;
import com.unravel.scout.utils.EndpointRoutesV1;
import com.unravel.scout.utils.annotataions.ApiController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@ApiController
@RequestMapping(EndpointRoutesV1.ROOT_ENDPOINT)
@Api(
    value = EndpointRoutesV1.ROOT_ENDPOINT,
    tags = {"Users"})
public class UserController {

  private final TripService tripService;
  private final UserService userService;

  @Autowired
  public UserController(final TripService itineraryService, final UserService userService) {
    this.tripService = itineraryService;
    this.userService = userService;
  }

  @GetMapping(EndpointRoutesV1.USERS_TRIP)
  @ApiOperation(value = "Get Trip or Jointly for a user")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_FETCHED,
      response = UserTripsDto.class)
  public ResponseEntity<?> getUsersTrip(
      @PathVariable String userId,
      @RequestParam(value = "is_jointly", required = false, defaultValue = "false")
          Boolean isJointly) {
    log.info(String.format("GET %s => userId: %s", EndpointRoutesV1.USERS_TRIP, userId));
    UserTripsDto itr = tripService.getTripsForUser(userId, isJointly);
    log.info("SUCCESS");
    return new ResponseEntity(
        new RestResponseWrapper<>(1, itr, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);
  }

  @PostMapping(EndpointRoutesV1.SHARE)
  @ApiOperation(value = "Share a Trip with registered user")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_FETCHED,
      response = TripDto.class,
      responseContainer = "RestResponseWrapper")
  public ResponseEntity<?> shareTrip(@RequestBody ShareTripRequest shareItineraryRequest) {
    log.info(String.format("POST %s", EndpointRoutesV1.SHARE));
    log.info(shareItineraryRequest.toString());
    try {
      TripDto trip = tripService.share(shareItineraryRequest);
      log.info("SUCCESS");
      return new ResponseEntity(
          new RestResponseWrapper<>(1, trip, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);
    } catch (ResourceNotFoundException e) {
      log.info("FAILED");
      return new ResponseEntity(
          new RestResponseWrapper<>(1, e, ApiResponseMessages.ITEM_FETCHED), HttpStatus.NOT_FOUND);
    }
  }

  @PostMapping(EndpointRoutesV1.LIKE_TRIP_ITEM)
  @ApiOperation(value = "like an Item in Trip")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_FETCHED,
      response = TripDto.class,
      responseContainer = "RestResponseWrapper")
  public ResponseEntity<?> likeTrip(@RequestBody LikeRequest likeRequest) {
    log.info(String.format("POST %s", EndpointRoutesV1.LIKE_TRIP_ITEM));
    log.info(likeRequest.toString());
    return new ResponseEntity(
        new RestResponseWrapper<>(
            1, tripService.like(likeRequest), ApiResponseMessages.ITEM_FETCHED),
        HttpStatus.OK);
  }

  @GetMapping(EndpointRoutesV1.USER_RECENTLY_VIEWED)
  @ApiOperation(value = "get recently Viewed Trips")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_FETCHED,
      response = TripDto.class,
      responseContainer = "RestResponseWrapper")
  public ResponseEntity<?> getUsersRecentlyViewedTrips(@PathVariable String userId) {
    log.info(String.format("GET %s => userId: %s", EndpointRoutesV1.USER_RECENTLY_VIEWED, userId));
    List<TripDto> itrs = tripService.getUsersRecentlyViewedTrips(userId);
    log.info("SUCCESS");
    return new ResponseEntity(
        new RestResponseWrapper<>(1, itrs, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);
  }

  @PostMapping(EndpointRoutesV1.USER_VIEW)
  @ApiOperation(value = "save views on Trips to populate the recently Viewed Trips")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_FETCHED,
      response = TripDto.class,
      responseContainer = "RestResponseWrapper")
  public ResponseEntity<?> addRecentlyViewedTrips(
      @RequestParam(name = "userId") String userId,
      @RequestParam(name = "tripId") String tripId,
      @RequestParam(name = "timestamp") DateTime timestamp) {
    log.info(
        String.format(
            "GET %s => userId: %s, ItrId: %s", EndpointRoutesV1.USER_VIEW, userId, tripId));
    User user = userService.addRecentlyViewedTripIds(userId, tripId, timestamp);
    log.info("SUCCESS");
    return new ResponseEntity(
        new RestResponseWrapper<>(1, user, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);
  }
}
