package com.unravel.scout.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.v1.*;
import com.unravel.scout.model.entity.Trip;
import com.unravel.scout.service.TripItineraryService;
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

import java.util.UUID;

@Log4j2
@ApiController
@Api(value = EndpointRoutesV1.Itinerary.ROOT, tags = {"Trip Itinerary"})
@RestController
@RequestMapping(EndpointRoutesV1.ROOT_ENDPOINT)
public class TripItineraryController {

    private final TripItineraryService tripItineraryService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TripItineraryController(TripItineraryService tripItineraryService) {
        this.tripItineraryService = tripItineraryService;
        this.objectMapper = new ObjectMapper();
    }

    @ApiOperation(value = "Get Trip Itinerary")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = UnravelResponse.class)
    @GetMapping(EndpointRoutesV1.TRIP_ITINERARY)
    public ResponseEntity<UnravelResponse> getTripItinerary(@RequestParam String tripId,
                                                            @RequestParam String userId) throws JsonProcessingException {
        log.info("Trip Itinerary requested for tripId: {} userId: {}", tripId, userId);
        UnravelResponse response = tripItineraryService.getTripItinerary(tripId, userId);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(EndpointRoutesV1.Itinerary.ADD_DAY)
    @ApiOperation(value = "Add new day to Trip")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_UPDATED, response = UnravelResponse.class)
    public ResponseEntity<UnravelResponse> addNewDayToTrip(
            @RequestBody AddNewDayToTripRequest addNewDayToTripRequest) throws JsonProcessingException {

        log.info("Request to Add new day to trip: {}", objectMapper.writeValueAsString(addNewDayToTripRequest));
        UnravelResponse response = tripItineraryService.addNewDayToTrip(addNewDayToTripRequest);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(EndpointRoutesV1.Itinerary.REMOVE_DAY)
    @ApiOperation(value = "Remove a day from Trip")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_DELETE, response = UnravelResponse.class)
    public ResponseEntity<UnravelResponse> removeDayFromTrip(
            @RequestBody RemoveADayFromTripRequest request) throws JsonProcessingException {
        log.info("Request to remove a day from Trip: {}", objectMapper.writeValueAsString(request));
        UnravelResponse response = tripItineraryService.removeDayFromTrip(request);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(EndpointRoutesV1.Itinerary.ADD_ITEM)
    @ApiOperation(value = "Add item to a day in Trip")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_UPDATED, response = UnravelResponse.class)
    public ResponseEntity<UnravelResponse> addItemToDay(
            @RequestBody AddItemToDayRequest request) throws JsonProcessingException {
        log.info("Request to add an item to a day in Trip: {}",
                objectMapper.writeValueAsString(request));
        UnravelResponse response = tripItineraryService.addItemToDay(request);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(EndpointRoutesV1.Itinerary.REMOVE_ITEM)
    @ApiOperation(value = "Remove item from a day in Trip")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_DELETE, response = UnravelResponse.class)
    public ResponseEntity<UnravelResponse> removeItemFromDay(
            @RequestBody RemoveItemFromDayRequest request) throws JsonProcessingException {
        log.info("Request to remove item from a day in Trip: {}",
                objectMapper.writeValueAsString(request));
        UnravelResponse response = tripItineraryService.removeItemFromDay(request);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(EndpointRoutesV1.Itinerary.REPLACE_ITEM)
    @ApiOperation(value = "Replace and item with another in Trip")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_UPDATED, response = UnravelResponse.class)
    public ResponseEntity<UnravelResponse> replaceItemInDay(
        @RequestBody ReplaceItemInDayRequest request) throws JsonProcessingException {
        log.info("Request to replace and item with another in Trip: {}",
                objectMapper.writeValueAsString(request));
        UnravelResponse response = tripItineraryService.replaceItemInDay(request);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(EndpointRoutesV1.Itinerary.UPDATE_DATES)
    @ApiOperation(value = "Update Trip Dates")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_UPDATED, response = UnravelResponse.class)
    public ResponseEntity<UnravelResponse> updateTripDates(
            @RequestBody UpdateTripDatesRequest request) throws JsonProcessingException {
        log.info("Request to update trip dates: {}",
                objectMapper.writeValueAsString(request));
        UnravelResponse response = tripItineraryService.updateTripDates(request);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(EndpointRoutesV1.Itinerary.UPDATE_DAYS)
    @ApiOperation(value = "Update Trip Days")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_UPDATED, response = UnravelResponse.class)
    public ResponseEntity<UnravelResponse> updateTripDates(
        @RequestBody UpdateTripDaysRequest request) throws JsonProcessingException {
        log.info("Request to update trip days: {}",
            objectMapper.writeValueAsString(request));
        UnravelResponse response = tripItineraryService.updateTripDays(request);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(EndpointRoutesV1.Itinerary.REORDER_ITEMS)
    @ApiOperation(value = "Re-order Trip Items")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_UPDATED, response = UnravelResponse.class)
    public ResponseEntity<UnravelResponse> reorderTripItems(
            @RequestBody ReorderTripItemsRequest request) throws JsonProcessingException {
        log.info("Request to re-order trip items: {}",
            objectMapper.writeValueAsString(request));
        UnravelResponse response = tripItineraryService.reorderTripItems(request);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(EndpointRoutesV1.FETCH_NEAR_BY_ITEMS)
    @ApiOperation(value = "fetch Items by Its SubType Id")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = Trip.class)
    public ResponseEntity<UnravelResponse> fetchItemsByItineraryDays(@RequestParam("userId") Long userId, @RequestParam("tripId") UUID tripId, @RequestParam("itineraryDays") int days) {
        log.info(String.format("GET %s => id: %s", EndpointRoutesV1.FETCH_NEAR_BY_ITEMS, tripId));
        UnravelResponse response = tripItineraryService.fetchItemsByItineraryDays(userId, tripId, days);
        log.info("SUCCESS");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
