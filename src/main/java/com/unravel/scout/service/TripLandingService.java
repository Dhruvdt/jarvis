package com.unravel.scout.service;

import com.unravel.constants.UnravelConstants;
import com.unravel.scout.model.dto.v1.CountryResponseDto;
import com.unravel.scout.model.dto.v1.ItemDetailDto;
import com.unravel.scout.model.dto.v1.RecentlyViewedResponseDto;
import com.unravel.scout.repositories.ItemDetailRepository;
import com.unravel.universal.UnravelResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class TripLandingService {
	 @Autowired
	 TripService tripService;
	 @Autowired
	 ItemDetailRepository itemDetailRepository;
	 @Autowired
	 RecentlyViewedService recentlyViewedService;
    
    public UnravelResponse fetchTripLandingPage(String userId, Boolean isJointly, int count) {

        UnravelResponse response = new UnravelResponse();

        List<CountryResponseDto> itemDetailDto = tripService.fetchCountryByItemType(userId,count);
        //2 : RecentlyViewed
        List<RecentlyViewedResponseDto> recentlyVieweditems = recentlyViewedService.recentlyViewedByUserId(userId,count);

        //3.UserTrip
        List<ItemDetailDto> userTrips = tripService.fetchUserTrips(userId, count);

        List<ItemDetailDto> recommendedTrip = tripService.fetchUserRecommendedTrip(userId, count);

        Map<String,Object> responseMap = new HashMap<>();
        
        responseMap.put("country_list", itemDetailDto);
        responseMap.put("recentlyViewed_list", recentlyVieweditems);
        responseMap.put("user_trip", userTrips);
        responseMap.put("recommended_trip", recommendedTrip);

        response.setMessage("Success");
        response.setStatus(UnravelConstants.TRUE);
        response.setData(responseMap);


        return response;
        
    }

}
