package com.unravel.scout.controller;

import java.util.List;

import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.TripDto;
import com.unravel.scout.model.dto.v1.RecentlyViewedResponseDto;
import com.unravel.scout.model.response.RestResponseWrapper;
import com.unravel.scout.service.RecentlyViewedService;
import com.unravel.scout.utils.EndpointRoutesV1;
import com.unravel.scout.utils.annotataions.ApiController;
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
@RequestMapping(EndpointRoutesV1.USER_END_POINT)
@Api(
		value = EndpointRoutesV1.USER_END_POINT,
		tags = {"User Recently Viewed"})
public class RecentlyViewedController {

	 @Autowired
	 RecentlyViewedService recentlyViewedService;


	@GetMapping(EndpointRoutesV1.USER_RECENTLY_VIEWED)
	@ApiOperation(value = "Get user recently viewed items")
	@ApiResponse(
			code = 200,
			message = ApiResponseMessages.RECENTLY_VIEWED,
			response = TripDto.class,
			responseContainer = "List")
	 public ResponseEntity<?> getAllSuggestionByUserId(@PathVariable (value = "userId") String userId, @RequestParam ("count") int count) {

		log.info(String.format("GET %s", EndpointRoutesV1.USER_RECENTLY_VIEWED));
		log.info(String.format("Get user recently viewed items::%s", userId));
		List<RecentlyViewedResponseDto> items = recentlyViewedService.recentlyViewedByUserId(userId,count);

		log.info("SUCCESS");
		return new ResponseEntity(
				new RestResponseWrapper<>(1, items, ApiResponseMessages.ITEM_FETCHED), HttpStatus.OK);

	  }
	 
	 
	
}
