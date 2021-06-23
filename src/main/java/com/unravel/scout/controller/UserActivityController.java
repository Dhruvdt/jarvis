package com.unravel.scout.controller;

import com.unravel.scout.service.TripService;
import com.unravel.scout.service.UserService;
import com.unravel.scout.utils.EndpointRoutesV1;
import com.unravel.scout.utils.annotataions.ApiController;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@Deprecated
@Log4j2
@ApiController
@RequestMapping(EndpointRoutesV1.ROOT_ENDPOINT)
@Api(
    value = EndpointRoutesV1.ROOT_ENDPOINT,
    tags = {"User Trip Interaction"})
public class UserActivityController {

  @Autowired private final TripService itineraryService;
  @Autowired private final UserService userService;

  public UserActivityController(final TripService itineraryService, final UserService userService) {
    this.itineraryService = itineraryService;
    this.userService = userService;
  }

  //  @GetMapping(EndpointRoutesV1.USER_RECENTLY_VIEWED)
  //  public ResponseEntity<?> getUsersRecentlyViewedTrips(@PathVariable String userId) {
  //    log.info(String.format("GET %s => userId: %s", EndpointRoutesV1.USER_RECENTLY_VIEWED,
  // userId));
  //    List<TripDto> itrs = itineraryService.getUsersRecentlyViewedTrips(userId);
  //    log.info("SUCCESS");
  //    return new ResponseEntity(itrs, HttpStatus.OK);
  //  }

  //  @PostMapping(EndpointRoutesV1.USER_VIEW)
  //  public ResponseEntity<?> addUserView(
  //      @RequestParam(name = "userId") String userId,
  //      @RequestParam(name = "ItineraryId") String itrId,
  //      @RequestParam(name = "timestamp") DateTime timestamp) {
  //    log.info(
  //        String.format(
  //            "GET %s => userId: %s, ItrId: %s", EndpointRoutesV1.USER_VIEW, userId, itrId));
  //    User user = userService.addRecentlyViewedTripIds(userId, itrId, timestamp);
  //    log.info("SUCCESS");
  //    return new ResponseEntity(user, HttpStatus.OK);
  //  }
}
