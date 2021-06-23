package com.unravel.scout.controller.v1;

import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.entity.Trip;
import com.unravel.scout.service.TripLandingService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@ApiController
@RequestMapping(EndpointRoutesV1.ROOT_ENDPOINT)
@Api(
    value = EndpointRoutesV1.ROOT_ENDPOINT,
    tags = {"Trip Landing Page"})
public class TripLandingController {
	@Autowired
	TripLandingService tripLandingService;

	  @GetMapping(EndpointRoutesV1.FETCH_TRIP_AND_MAPDATA)
	  @ApiOperation(value = "Get Trip Landing Page")
	  @ApiResponse(code = 200, message = ApiResponseMessages.ITEM_FETCHED, response = Trip.class)
	  public ResponseEntity<UnravelResponse> fetchTripLandingData(@RequestParam("userId") String userId,
			    @RequestParam(value = "is_jointly", required = false, defaultValue = "false") Boolean isJointly,int count) {

		  UnravelResponse response = tripLandingService.fetchTripLandingPage(userId, isJointly,count);

	    log.info("SUCCESS");
		  return new ResponseEntity<>(response, HttpStatus.OK);
	  }
	  
}
