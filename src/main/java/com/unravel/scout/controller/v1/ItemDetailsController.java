package com.unravel.scout.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.v1.GetRelatedTripsRequest;
import com.unravel.scout.service.ItemDetailService;
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

@Log4j2
@ApiController
@RequestMapping(EndpointRoutesV1.ItemsURI.ROOT)
@Api(value = EndpointRoutesV1.ROOT_ENDPOINT, tags = {"ItemDetails"})
public class ItemDetailsController {

    private final ItemDetailService itemDetailService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ItemDetailsController(ItemDetailService itemDetailService) {
        this.itemDetailService = itemDetailService;
        this.objectMapper = new ObjectMapper();
    }

    @ApiOperation(value = "View Item Details",response = UnravelResponse.class)
    @GetMapping(EndpointRoutesV1.ItemsURI.ItemDetails)
    public ResponseEntity<UnravelResponse> getItemById(@PathVariable String itemId,
                                                       @RequestParam String userId) {
        UnravelResponse response = itemDetailService.getItemById(itemId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Item Components by Name",response = UnravelResponse.class)
    @GetMapping(EndpointRoutesV1.ItemsURI.Components)
    public ResponseEntity<UnravelResponse> getItemComponentsByName(@RequestParam String itemId,
                                                                   @RequestParam String userId,
                                                                   @RequestParam String componentName) {
        UnravelResponse response = itemDetailService
                .getItemComponentsByName(itemId, userId, componentName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Spot Details of item on map",response = UnravelResponse.class)
    @GetMapping(EndpointRoutesV1.ItemsURI.ItemDetail)
    public ResponseEntity<UnravelResponse> getSpotDetails(@RequestParam("itemId") String itemId,
                                                       @RequestParam("userId") String userId) {
        UnravelResponse response = itemDetailService.getSpotDetails(itemId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Trips containing the given Items")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = UnravelResponse.class)
    @PostMapping(EndpointRoutesV1.ItemsURI.RelatedTrips)
    public ResponseEntity<UnravelResponse> getRelatedTrips(@RequestBody GetRelatedTripsRequest request)
            throws JsonProcessingException {
        log.info("GetRelatedTripsRequest: {}", objectMapper.writeValueAsString(request));
        UnravelResponse response = itemDetailService.getRelatedTrips(request);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Tells which user Trips contain the given Item")
    @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = UnravelResponse.class)
    @GetMapping(EndpointRoutesV1.ItemsURI.RelatedUserTrips)
    public ResponseEntity<UnravelResponse> getRelatedUserTrips(@RequestParam("itemId") String itemId,
                                                               @RequestParam("userId") String userId)
            throws JsonProcessingException {
        log.info("GetRelatedUserTrips Request. userId: {} itemId: {}", userId, itemId);
        UnravelResponse response = itemDetailService.getRelatedUserTrips(userId, itemId);
        log.debug("Response: {}", objectMapper.writeValueAsString(response));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
