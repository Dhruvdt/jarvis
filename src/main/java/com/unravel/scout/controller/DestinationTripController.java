package com.unravel.scout.controller;

import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.TripDto;
import com.unravel.scout.model.entity.Trip;
import com.unravel.scout.model.entity.v1.Country;
import com.unravel.scout.model.response.RestResponseWrapper;
import com.unravel.scout.service.DestinationsService;
import com.unravel.scout.service.TripService;
import com.unravel.scout.utils.EndpointRoutesV1;
import com.unravel.scout.utils.annotataions.ApiController;
import com.unravel.universal.UnravelResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@ApiController
@RequestMapping(EndpointRoutesV1.ROOT_ENDPOINT)
@Api(
    value = EndpointRoutesV1.ROOT_ENDPOINT,
    tags = {"Destinations Trip Search"})
public class DestinationTripController {
  private final TripService tripService;
  private final DestinationsService destinationsService;

  @Autowired
  public DestinationTripController(
      final TripService tripService, final DestinationsService destinationsService) {
    this.tripService = tripService;
    this.destinationsService = destinationsService;
  }
  @Deprecated
  @GetMapping(EndpointRoutesV1.DESTINATIONS_TRIP)
  @ApiOperation(value = "Get Trips for a given Destination")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_FETCHED,
      response = TripDto.class,
      responseContainer = "List")
  public ResponseEntity<?> getDestinationsItineraries(
      @PathVariable String destinationName,
      @RequestParam(value = "is_jointly", required = false, defaultValue = "false")
          Boolean isJointly) {
    log.info(String.format("GET %s", EndpointRoutesV1.DESTINATIONS_TRIP));
    log.info(String.format("DestinationName::%s", destinationName));
    List<TripDto> trips = tripService.getTripsForDestination(destinationName, isJointly);
    log.info("SUCCESS");
    return new ResponseEntity(
        new RestResponseWrapper<>(1, trips, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);
  }
  @Deprecated
  @GetMapping(EndpointRoutesV1.DESTINATIONS)
  @ApiOperation(value = "Get List of Destinations")
  @ApiResponse(
      code = 200,
      message = ApiResponseMessages.ITEM_FETCHED,
      response = String.class,
      responseContainer = "Set")
  public ResponseEntity<?> getDestinations() {
    log.info(String.format("GET %s", EndpointRoutesV1.DESTINATIONS));
    Set<String> destinations =
        destinationsService.getDestinationDocument().getItineraryMapping().values().stream()
            .map(m -> m.getNames().get(0))
            .collect(Collectors.toSet());
    log.info("SUCCESS");
    return new ResponseEntity(
        new RestResponseWrapper<>(1, destinations, ApiResponseMessages.ITEM_FETCHED),
        HttpStatus.OK);
  }

  @GetMapping(EndpointRoutesV1.DESTINATIONS_LIST)
  @ApiOperation(value = "Get List of Countries")
  @ApiResponse(
          code = 200,
          message = ApiResponseMessages.ITEM_FETCHED,
          response = String.class,
          responseContainer = "Set")
  public ResponseEntity<?> getDestinationsList(@RequestParam int page, @RequestParam int size) {
    Pageable pageable = PageRequest.of(page, size);
      log.info(String.format("GET %s", EndpointRoutesV1.DESTINATIONS_LIST));
    List<Country> countries =  destinationsService.getCountriesDocument(pageable);

      log.info("SUCCESS");
      return new ResponseEntity(
              new RestResponseWrapper<>(1, countries, ApiResponseMessages.ITEM_FETCHED),
              HttpStatus.OK);
  }


  //fetch destination

  @GetMapping(EndpointRoutesV1.FETCH_DESTINATION_CATEGORY)
  @ApiOperation(value = "fetch destinations")
  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = Trip.class)
  public ResponseEntity<UnravelResponse> fetchCategory(@RequestParam("country_id") String countryId) {
    log.info(String.format("GET %s => id: %s", EndpointRoutesV1.FETCH_DESTINATION_CATEGORY, countryId));
    Object response = destinationsService.fetchDestinationByItemType(countryId);
    log.info("SUCCESS");
    return new ResponseEntity(
        new RestResponseWrapper<>(1, response, ApiResponseMessages.ITEM_FETCHED),
        HttpStatus.OK);
  }
}
