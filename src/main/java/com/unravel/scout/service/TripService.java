package com.unravel.scout.service;

import com.unravel.constants.UnravelConstants;
import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.ChangeLogItemDto;
import com.unravel.scout.model.dto.TripDto;
import com.unravel.scout.model.dto.UserTripsDto;
import com.unravel.scout.model.dto.v1.*;
import com.unravel.scout.model.entity.DestinationMapping;
import com.unravel.scout.model.entity.Trip;
import com.unravel.scout.model.entity.User;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.ItemSubType;
import com.unravel.scout.model.enums.DocumentType;
import com.unravel.scout.model.enums.ItemType;
import com.unravel.scout.model.enums.Permissions;
import com.unravel.scout.model.exceptions.ResourceNotFoundException;
import com.unravel.scout.model.request.LikeRequest;
import com.unravel.scout.model.request.ShareTripRequest;
import com.unravel.scout.model.request.TripChangeRequest;
import com.unravel.scout.model.response.RestResponseWrapper;
import com.unravel.scout.repositories.ItemDetailRepository;
import com.unravel.scout.repositories.ItemSubTypeRepository;
import com.unravel.scout.repositories.TripsRepository;
import com.unravel.scout.repository.TripRepository;
import com.unravel.scout.utils.IdsUtils;
import com.unravel.universal.UnravelResponse;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TripService {
    private final TripRepository tripRepository;    // Mongo
    private final TripsRepository tripsRepository;  // JPA
    private final TripUpdateService tripUpdateService;
    private final DestinationsService destinationsService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ItemDetailRepository itemDetailRepository;
    private final ItemSubTypeRepository itemSubTypeRepository;

    @Autowired
    public TripService(final TripRepository tripRepository,
                       final TripsRepository tripsRepository,
                       final TripUpdateService tripUpdateService,
                       final DestinationsService destinationsService,
                       final UserService userService,
                       final ModelMapper modelMapper,
                       ItemDetailRepository itemDetailRepository,
                       ItemSubTypeRepository itemSubTypeRepository) {
        this.tripRepository = tripRepository;
        this.tripsRepository = tripsRepository;
        this.tripUpdateService = tripUpdateService;
        this.destinationsService = destinationsService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.itemDetailRepository = itemDetailRepository;
        this.itemSubTypeRepository = itemSubTypeRepository;
    }

    public TripDto getTripDto(String id) {
        Trip trip =
            tripRepository
                .findBy_id(IdsUtils.getIdWithPrefix(id, DocumentType.TRIP))
                .orElseThrow(() -> new ResourceNotFoundException("Trip"));
        return modelMapper.map(trip, TripDto.class);
    }

    public Trip getTrip(String id) {
        return tripRepository
            .findBy_id(IdsUtils.getIdWithPrefix(id, DocumentType.TRIP))
            .orElseThrow(() -> new ResourceNotFoundException("Trip"));
    }

    public List<TripDto> getTripsForDestination(String city, boolean isJointly) {
        DestinationMapping dm = destinationsService.getDestinationMapping(city);
        if (dm == null) return new ArrayList<>();
        return dm.getUnravelTrips().stream()
            .map(this::getTripDto)
            .filter(f -> f.isJointly() == isJointly)
            .collect(Collectors.toList());
    }

    public List<TripDto> getUsersRecentlyViewedTrips(User user) {
        if (user.getSharedWithMe() != null) {
            return user.getRecentlyViewed().keySet().stream()
                .map(this::getTripDto)
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<TripDto> getUsersRecentlyViewedTrips(String userId) {
        return userService.getUsersRecentlyViewedTripIds(userId).keySet().stream()
            .map(this::getTripDto)
            .collect(Collectors.toList());
    }

    private List<TripDto> getSharedTrips(User user) {
        if (user.getSharedWithMe() != null) {
            return user.getSharedWithMe().stream().map(this::getTripDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<TripDto> getUsersTrip(User user, boolean isJointly) {
        try {
            if (user.getMyItineraries() != null) {
                return user.getMyItineraries().stream()
                    .map(this::getTripDto)
                    .filter(f -> f.isJointly() == isJointly)
                    .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (ResourceNotFoundException e) {
            log.error("Resource Not Found");
            throw e;
        }
    }

    private List<TripDto> getSuggestedTrips(User user) {
        if (user.getSuggested() != null) {
            return user.getSuggested().stream().map(this::getTripDto).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public UserTripsDto getTripsForUser(String userId, boolean isJointly) {
        User user = userService.getOrCreateUserDocument(userId);

        return UserTripsDto.builder()
            .userId(userId)
            .myTrips(getUsersTrip(user, isJointly))
            .sharedWithMe(getSharedTrips(user))
            .suggested(getSuggestedTrips(user))
            .recentlyViewed(getUsersRecentlyViewedTrips(user))
            .build();
    }

    public TripDto share(ShareTripRequest shareTripRequest) {
        Trip trip =
            tripRepository
                .findBy_id(shareTripRequest.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip"));
        if (trip.getPermission().getOwner().equals(shareTripRequest.getOwnerId())) {
            shareTripRequest
                .getShareWith()
                .forEach(
                    userId -> {
                        trip.getPermission().getSharedWIth().put(userId, Permissions.SUGGEST);
                        userService.shareItinerary(userId, trip);
                    });
            return modelMapper.map(tripRepository.save(trip), TripDto.class);
        } else {
            throw new ResourceNotFoundException("Owner");
        }
    }

    public Trip update(TripChangeRequest tripChangeRequest, Trip trip) {
        if (tripChangeRequest.getChangeLogItemDto() == null) {
            trip = tripUpdateService.updateTripBasicInfo(trip, tripChangeRequest);
            return trip;
        } else {
            for (Map.Entry<String, ChangeLogItemDto> entry :
                tripChangeRequest.getChangeLogItemDto().entrySet()) {
                trip = tripUpdateService.update(trip, entry.getValue());
            }
            return trip;
        }
    }

    public Trip update(TripChangeRequest tripChangeRequest) {
        Trip trip = getTrip(tripChangeRequest.getTripId());
        if (!trip.isDefault()) {
            Trip itinerary = update(tripChangeRequest, trip);
            return tripRepository.save(itinerary);
        }
        return null;
    }

    public Trip like(LikeRequest likeRequest) {
        return like(
            likeRequest.getUserId(),
            likeRequest.getTimestamp(),
            likeRequest.getTripId(),
            likeRequest.getTripItemId());
    }

    private Trip like(String userId, String timestamp, String tripId, String tripItemId) {
        Trip trip = getTrip(tripId);
        trip.getTripDays()
            .forEach(
                tripDay ->
                    tripDay.getItineraryItems().stream()
                        .peek(
                            item -> {
                                if (item.getItemId().equals(tripItemId)) {
                                    item.getLikes().setTotalCount(item.getLikes().getTotalCount() + 1);
                                    if (item.getLikes().getLikes() == null) {
                                        Map<String, String> tempLike = new HashMap<>();
                                        tempLike.put(userId, timestamp);
                                        item.getLikes().setLikes(tempLike);
                                    } else {
                                        item.getLikes().getLikes().put(userId, timestamp);
                                    }
                                }
                            }));
        return tripRepository.save(trip);
    }

    public void deleteTrip(String tripId) {
        tripRepository.deleteBy_id(tripId);
    }

    /*New Api's*/

    public List<ItemDetailDto> fetchTripDto(String userId, int count) {
        List<ItemDetail> items = null;
        // user id mapping is not there so the below code is comment out
   /* if (count != 5 )
     items= itemDetailsRepository.fetchData("TRIP");

    else{*/
        Pageable pageable = PageRequest.of(0, 5);
        items = itemDetailRepository.findAll(pageable).toList();
        //  }
      //  List<TripInformationsDto> tripInfoDto = ObjectMapperUtils.mapAll(items, TripInformationsDto.class);

        return items.stream().map(ItemDetailDto::mapToItemDetailDtoBasicOnly).collect(Collectors.toList());
       // return tripInfoDto;
    }

    public UnravelResponse getTripItinerary(String tripId, String userId) {
        UnravelResponse response = new UnravelResponse();


        Optional<ItemDetail> tripDetails = this.itemDetailRepository.findById(UUID.fromString(tripId));

        if(tripDetails.isEmpty() || tripDetails.get().getTripDays().isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }

        ItemDetail trip = tripDetails.get();
        ItemDetailDto tripDto = ItemDetailDto.mapToItemDetailDtoBasicOnly(trip);
        // List<ItineraryItem> items = tripsRepository.findByTripId(UUID.fromString(tripId));

        TreeMap<Integer, List<ItineraryItemDto>> itineraryItemsByDay = trip.getTripDays().stream()
            .map(ItineraryItemDto::mapToItineraryItemDto)
            .collect(
                Collectors.groupingBy(ItineraryItemDto::getDay, TreeMap::new, Collectors.toList())
            );

        List<TripDayDto> tripDays = itineraryItemsByDay.entrySet().stream()
            .map(e -> TripDayDto.getTripDayDto(e.getKey(), e.getValue()))
            .collect(Collectors.toList());

        tripDto.setTripDays(tripDays);

        response.setMessage("Success");
        response.setStatus(UnravelConstants.TRUE);
        response.setData(tripDto);
        return response;
    }

    public List<ItemDetailDto> fetchUserRecommendedTrip(String userId, int count) {
        List<ItemDetail> items = new ArrayList<>();
        if (count == 0) {
            items = itemDetailRepository.findByItemTypeAndIsDefault(ItemType.TRIP.toString(),Boolean.TRUE);
        } else {
            Pageable pageable = PageRequest.of(0, count);
            items = itemDetailRepository.findByItemTypeAndIsDefault(ItemType.TRIP.toString(), pageable,Boolean.TRUE);
        }
        return items.stream().map(ItemDetailDto::mapToItemDetailDtoBasicOnly).collect(Collectors.toList());

    }

    public List<ItemDetailDto> fetchUserTrips(String userId, int count) {
        List<ItemDetail> items = new ArrayList<>();

        if (count == 0) {
            items = itemDetailRepository.findByItemTypeAndIsDefaultAndUserId(
                ItemType.TRIP.toString(), false, Long.parseLong(userId));
        } else {
            Pageable pageable = PageRequest.of(0, count, Sort.by("createdAt"));
            items = itemDetailRepository.findByItemTypeAndIsDefaultAndUserId(
                ItemType.TRIP.toString(), pageable, false, Long.parseLong(userId));

            /*if (!items.isEmpty()) {
                List<ItineraryItem> itineraryItemList = tripsRepository.findByTripId(items.get(0).getId());
            }*/
        }
        return items.stream().map(ItemDetailDto::mapToItemDetailDtoWithItineraryItem).collect(Collectors.toList());
    }

    public  List<CountryResponseDto> fetchCountryByItemType(String userId, int count) {
    	
        List<ItemDetail> items = new ArrayList<>();
        if(count==0) {
            items = itemDetailRepository.findByItemType(ItemType.COUNTRY.toString());
        }
        else
        {
            Pageable pageable = PageRequest.of(0, count);
            items = itemDetailRepository.findByItemType(ItemType.COUNTRY.toString(),pageable);
        }
        return	items.stream().map(CountryResponseDto::getCountryList).collect(Collectors.toList());
    }

    public UnravelResponse fetchCategoryByTripId(Optional<String> tripId, Optional<Integer> page,Optional<Integer> size) {
        Pageable paging;
        Page<ItemSubType> categoryTrip = null ;
        List<ItemDetail> tripItems= new ArrayList<>();
        UnravelResponse response = new UnravelResponse();

        
        if(!tripId.isPresent()) {
          	 log.info("Failed");
          	  response.setStatus(UnravelConstants.FALSE);
              response.setMessage(ApiResponseMessages.TRIP_ID_NULL);
              return response;
          }
        else if(!page.isPresent()) {
         	 log.info("Failed");
         	  response.setStatus(UnravelConstants.FALSE);
             response.setMessage(ApiResponseMessages.PAGE_NOT_FOUND);
             return response;
         }
        
        else if(!size.isPresent()) {
        	 log.info("Failed");
        	  response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.SIZE_NOT_FOUND);
            return response;
        }
        
      
        if(size.get()==0 || page.get()== 0)
            paging = Pageable.unpaged();
        else
            paging = PageRequest.of(page.get(), size.get());

        if(tripId.get().isEmpty())
            categoryTrip = itemSubTypeRepository.findAll(paging);
        else
            tripItems = itemDetailRepository.findByItemTypeAndId(ItemType.TRIP.toString(),UUID.fromString(tripId.get()),paging);


        if(tripId.get().isEmpty()){
            response.setData(CategoryDetailDto.mapToCategoryListDto(categoryTrip.getContent()));
            if(categoryTrip==null){
                log.debug("No item found for  trip-id: {} userId: {}", tripId);
                response.setStatus(UnravelConstants.FALSE);
                response.setMessage("No Record found");
                return response;
            }
        }
        else{
            if(tripItems==null){
                log.debug("No item found for  trip-id: {} userId: {}", tripId);
                response.setStatus(UnravelConstants.FALSE);
                response.setMessage("No Record found");
                return response;
            }
            response.setData(tripItems.stream().map(CategoryItemDetailDto::mapToCategoryItemDetailDtoWithSubType).collect(Collectors.toList()));
        }

        response.setMessage("Success");
        response.setStatus(UnravelConstants.TRUE);
        return response;
    }

    public UnravelResponse renameTrip(RenameTripRequest request) {
        UnravelResponse response = new UnravelResponse();

        var tripDetail = tripsRepository.findByTripId(UUID.fromString(request.getTripId()));

        if (tripDetail.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }

        int updated = tripsRepository.updateTripName(UUID.fromString(request.getTripId()), request.getDisplayName());

        if (updated != 1) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.OPERATION_FAILED);
            return response;
        }

        var newtripDetails = itemDetailRepository.findById(UUID.fromString(request.getTripId()));
        var updatedTrip = newtripDetails.get();

        ItemDetailDto tripDto = ItemDetailDto.mapToItemDetailDtoBasicOnly(updatedTrip);

        TreeMap<Integer, List<ItineraryItemDto>> itineraryItemsByDay = updatedTrip.getTripDays().stream()
            .map(ItineraryItemDto::mapToItineraryItemDto)
            .collect(
                Collectors.groupingBy(ItineraryItemDto::getDay, TreeMap::new, Collectors.toList())
            );

        List<TripDayDto> tripDays = itineraryItemsByDay.entrySet().stream()
            .map(e -> TripDayDto.getTripDayDto(e.getKey(), e.getValue()))
            .collect(Collectors.toList());

        tripDto.setTripDays(tripDays);

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_UPDATED);
        response.setData(tripDto);

        return response;
    }

    public  UnravelResponse fetchItemsBySubType(Optional<String> subtypeId, Optional<Integer> size) {
    	
        UnravelResponse response = new UnravelResponse();
       
    	 if(!subtypeId.isPresent() || subtypeId.get() == null || subtypeId.get().equals("")) {
    	   	 	log.info("Failed");
	    	   	response.setStatus(UnravelConstants.FALSE);
	            response.setMessage( ApiResponseMessages.SUB_TYPE_NULL);
            return response;
    	   }
    	   else if (!size.isPresent() || size==null || size.equals("")){
    		   log.info("Failed");
	    	   	response.setStatus(UnravelConstants.FALSE);
	            response.setMessage( ApiResponseMessages.SIZE_NOT_FOUND);
           return response;
    	   }
    	   else
    	   {
    	    	List<ItemDetail> items =  itemDetailRepository.findTopN(Long.valueOf(subtypeId.get()),size.get());
    	        if(items.isEmpty()){
    	            log.debug("No item found for  sub type-id: {} ", subtypeId);
    	            response.setStatus(UnravelConstants.FALSE);
    	            response.setMessage("No Record found");
    	            return response;
    	        }
    	        response.setMessage("Success");
    	        response.setStatus(UnravelConstants.TRUE);
    	        response.setData(items.stream().map(ItemDetailDto::mapToItemDetailDtoSubType).collect(Collectors.toList()));
    	        return response;   
    	   }
    }
}
