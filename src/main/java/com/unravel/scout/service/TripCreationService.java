package com.unravel.scout.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unravel.constants.UnravelConstants;
import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.TripDto;
import com.unravel.scout.model.entity.Address;
import com.unravel.scout.model.entity.DestinationMapping;
import com.unravel.scout.model.entity.Permission;
import com.unravel.scout.model.entity.Trip;
import com.unravel.scout.model.entity.v1.CountryItemDestinationMapping;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.ItineraryItem;
import com.unravel.scout.model.enums.DocumentType;
import com.unravel.scout.model.enums.ItemType;
import com.unravel.scout.model.request.TripChangeRequest;
import com.unravel.scout.model.request.TripCreationRequest;
import com.unravel.scout.model.request.v1.CreateTripDto;
import com.unravel.scout.repositories.CountryMapping;
import com.unravel.scout.repositories.TripsRepository;
import com.unravel.scout.repositories.ItemDetailRepository;
import com.unravel.scout.repository.TripRepository;
import com.unravel.scout.utils.DateTimeUtils;
import com.unravel.scout.utils.IdsUtils;
import com.unravel.universal.UnravelResponse;
import lombok.extern.log4j.Log4j2;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TripCreationService {
    private final TripService tripService;
    private final TripRepository tripRepository;
    private final DestinationsService destinationsService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TripsRepository tripsRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final CountryMapping countryMappingRepo;
    private final TripItineraryService tripItineraryService;

    @Autowired
    public TripCreationService(
        final TripService tripService,
        final TripRepository tripRepository,
        final DestinationsService destinationsService,
        final ModelMapper modelMapper,
        final UserService userService,
        final TripsRepository tripsRepository,
        final ItemDetailRepository itemDetailRepository,
        final CountryMapping countryMappingRepo,
        final TripItineraryService tripItineraryService
    ) {
        this.tripService = tripService;
        this.tripRepository = tripRepository;
        this.destinationsService = destinationsService;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.tripsRepository = tripsRepository;
        this.itemDetailRepository = itemDetailRepository;
        this.countryMappingRepo = countryMappingRepo;
        this.tripItineraryService = tripItineraryService;
    }

    private int requestedDuration(TripCreationRequest tripCreationRequest) {
        if (tripCreationRequest.getDurationDays() != 0) {
            return tripCreationRequest.getDurationDays();
        } else if (tripCreationRequest.getStartDate() != null
            && tripCreationRequest.getEndDate() != null) {
            LocalDate startDate = DateTimeUtils.string2Date(tripCreationRequest.getStartDate());
            LocalDate endDate = DateTimeUtils.string2Date(tripCreationRequest.getEndDate());
            return Days.daysBetween(startDate, endDate).getDays();
        }
        return 0;
    }

    public Optional<TripDto> createUserTrip(TripCreationRequest tripCreationRequest) {
        try {
            Trip trip =
                (tripCreationRequest.getTripId() == null)
                    ? findAndCopyTrip(tripCreationRequest)
                    : createUserTripFromUnravelTrip(
                    tripCreationRequest, tripService.getTrip(tripCreationRequest.getTripId()));

            if (trip != null) {
                trip =
                    tripService.update(
                        TripChangeRequest.builder()
                            .tripId(trip.getId())
                            .displayName(tripCreationRequest.getTripName())
                            .startDate(tripCreationRequest.getStartDate())
                            .endDate(tripCreationRequest.getEndDate())
                            .durationDays(requestedDuration(tripCreationRequest))
                            .build(),
                        trip);
                tripRepository.save(trip);
                userService.addOwnedTripIds(tripCreationRequest.getUserId(), trip.getId());
                return Optional.of(modelMapper.map(trip, TripDto.class));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            log.info(e.getLocalizedMessage());
            return Optional.empty();
        }
    }

    private Trip findAndCopyTrip(TripCreationRequest tripCreationRequest) {
        DestinationMapping dm =
            destinationsService.getDestinationMapping(tripCreationRequest.getDestination());
        if (dm == null) {
            return null;
        }
        String tripId = dm.getUnravelTrips().get(0);
        return createUserTripFromUnravelTrip(tripCreationRequest, tripService.getTrip(tripId));
    }

    private Trip createNewTrip(TripCreationRequest tripCreationRequest) {
        // todo: destination should be and object with continent, country etc
        String id = IdsUtils.randomId();
        return Trip.builder()
            ._id(IdsUtils.getIdWithPrefix(id, DocumentType.TRIP))
            .id(id)
            .startDate(tripCreationRequest.getStartDate())
            .endDate(tripCreationRequest.getEndDate())
            .durationDays(tripCreationRequest.getDurationDays())
            .isJointly(tripCreationRequest.isJointly())
            .userInput(tripCreationRequest.getQuestions())
            .address(Address.builder().city(tripCreationRequest.getDestination()).build())
            .permission(
                Permission.builder()
                    .owner(tripCreationRequest.getUserId())
                    .sharedWIth(new HashMap<>())
                    .build())
            .build();
    }

    private Trip createUserTripFromUnravelTrip(TripCreationRequest tripCreationRequest, Trip trip) {
        String id = IdsUtils.randomId();
        trip.setId(id);
        trip.set_id(IdsUtils.getIdWithPrefix(id, DocumentType.TRIP));
        trip.setDefault(false);
        trip.setCreatedAt(DateTimeUtils.currentDatetimeString());
        trip.setModifiedAt(DateTimeUtils.currentDatetimeString());
        trip.setDurationDays(trip.getDurationDays());
        trip.setStartDate(tripCreationRequest.getStartDate());
        trip.setEndDate(tripCreationRequest.getEndDate());
        trip.setUserInput(tripCreationRequest.getQuestions());
        trip.setJointly(tripCreationRequest.isJointly());
        trip.setPermission(
            Permission.builder()
                .owner(tripCreationRequest.getUserId())
                .sharedWIth(new HashMap<>())
                .build());
        return trip;
    }


    public UnravelResponse processingUserTrip(CreateTripDto tripCreationRequest) {
        UnravelResponse response = new UnravelResponse();
        if (tripCreationRequest.getUser_id() == null || tripCreationRequest.getUser_id().equals("")) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.USER_ID_NULL);
            return response;
        } else if ((tripCreationRequest.getTrip_id() != null && tripCreationRequest.getTrip_id().equals("")) || (tripCreationRequest.getDestination() != null && tripCreationRequest.getDestination().equals(""))) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.TRIP_NOT_CREATED);
            return response;
        } else {
            try {
                UnravelResponse tripInfoDto = findAndCopyExistingUnravelIternaries(tripCreationRequest);
                if (tripInfoDto != null) {
                    return tripInfoDto;
                }
            } catch (Exception e) {
                log.info(e.getLocalizedMessage());
                return null;

            }
        }

        return null;
    }

    private ItemDetail copyTripDetail(Optional<ItemDetail> copyObject, CreateTripDto tripCreationRequest) throws JsonProcessingException {
        ItemDetail itemDetail = copyObject.get();
        ItemDetail itemDetail1 = new ItemDetail();
        UUID uuid = UUID.randomUUID();
        itemDetail1.setItemType(ItemType.TRIP.toString());
        itemDetail1.setId(uuid);
        itemDetail1.setName((tripCreationRequest.getTrip_name() != null && !tripCreationRequest.getTrip_name().isEmpty()) ? tripCreationRequest.getTrip_name() : itemDetail.getName());
        itemDetail1.setDescription(itemDetail.getDescription());
        itemDetail1.setVendorId(itemDetail.getVendorId());
        itemDetail1.setShareUrl(itemDetail.getShareUrl());
        itemDetail1.setAddress(itemDetail.getAddress());
        itemDetail1.setDisplayName(itemDetail.getDisplayName());
        for (var itemImageMapping: itemDetail.getImages()) {
            itemDetail1.addExistingImage(itemImageMapping);
        }
        // itemDetail1.setImages(itemDetail.getImages());
        for (var itemComponentMapping: itemDetail.getComponents()) {
            itemDetail1.addExistingComponent(itemComponentMapping);
        }
        for (var itemVideoMapping: itemDetail.getVideos()) {
            itemDetail1.addExistingVideo(itemVideoMapping);
        }
        // itemDetail1.setComponents(itemDetail.getComponents());
        // itemDetail1.setCountryDestinations(itemDetail.getCountryDestinations());
        itemDetail1.setUserId(Long.parseLong(tripCreationRequest.getUser_id()));
        itemDetail1.setLatitude(itemDetail.getLatitude());
        itemDetail1.setLongitude(itemDetail.getLongitude());
        itemDetail1.setIsDefault(Boolean.FALSE);
        if (tripCreationRequest.getQuestions() != null && !tripCreationRequest.getQuestions().isEmpty()) {
            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(itemDetail.getProperties());
            ObjectNode node = (ObjectNode) mapper.readTree(json);
            node.putPOJO("questions", tripCreationRequest.getQuestions());
            String json1 = json;
            itemDetail1.setProperties(node);
        } else {
            itemDetail1.setProperties(itemDetail.getProperties());
        }
        itemDetail1.setItemSubType(itemDetail.getItemSubType());
        if (tripCreationRequest.getDuration_days() == 0){
            tripCreationRequest.setDuration_days(itemDetail.getTripDays().size());
        }
        itemDetail1.setTripDays(insertItinerary(itemDetail, tripCreationRequest, itemDetail1));

        updateProperties(itemDetail1,itemDetail1.getTripDays().size(),tripCreationRequest);

        return itemDetailRepository.save(itemDetail1);
    }

    public UnravelResponse createItineraries(ItemDetail itemDetail, CreateTripDto tripCreationRequest) {
       return tripService.getTripItinerary(itemDetail.getId().toString(),tripCreationRequest.getUser_id());
    }

    private UnravelResponse findAndCopyExistingUnravelIternaries(CreateTripDto tripCreationRequest) throws JsonProcessingException {
        Optional<ItemDetail> copyObject = null;
        Optional<CountryItemDestinationMapping> countryItemDestinationMapping = null;
        //fetch default country
        if (tripCreationRequest.getDestination() != null && !tripCreationRequest.getDestination().equals("")) {
            countryItemDestinationMapping = countryMappingRepo.findByDestinationId(IdsUtils.convertStringToUUID(tripCreationRequest.getDestination()));
            if (!countryItemDestinationMapping.isPresent()) {
                return null;
            }
            copyObject = itemDetailRepository.findById(countryItemDestinationMapping.get().getDefaultTrip().getId());
        } else {
            copyObject = itemDetailRepository.findById(IdsUtils.convertStringToUUID(tripCreationRequest.getTrip_id()));
        }
        ItemDetail tripDetail = copyTripDetail(copyObject, tripCreationRequest);
        return createItineraries(tripDetail, tripCreationRequest);
    }

    private Set<ItineraryItem> insertItinerary(ItemDetail oldTrip, CreateTripDto tripCreationRequest, ItemDetail newTrip) {

        Set<ItineraryItem> oldTripItinerary = oldTrip.getTripDays();
        Set<ItineraryItem> newTripItinerary = new HashSet<>();

        int newDayCount = tripCreationRequest.getDuration_days();
        int existingDayCount = oldTrip.getTripDays().size();

        TreeMap<Integer, List<ItineraryItem>> itineraryItemsByDay = oldTrip.getTripDays().stream()
            .collect(
                Collectors.groupingBy(ItineraryItem::getDay, TreeMap::new, Collectors.toList())
            );

        itineraryItemsByDay.values().stream().limit(newDayCount).forEach(itineraryItems -> {
            itineraryItems.forEach(itineraryItem -> {
                ItineraryItem itineraryItem1 = itineraryItem.getCopy();
                itineraryItem1.setTrip(newTrip);
                if (tripCreationRequest.getStart_date() != null && !tripCreationRequest.getStart_date().isEmpty())
                itineraryItem1.setStartTime(tripCreationRequest.getStart_date());
                if (tripCreationRequest.getEnd_date() != null && !tripCreationRequest.getEnd_date().isEmpty())
                itineraryItem1.setEndTime(tripCreationRequest.getEnd_date());
                newTripItinerary.add(itineraryItem1);
            });
        });
        if (newDayCount > existingDayCount) {

            List<ItineraryItem> firstDayItems = itineraryItemsByDay.get(1);
            for (int i = (newDayCount + 1); i <= existingDayCount; i++) {
                int finalI = i;
                firstDayItems.forEach(itineraryItem -> {
                    ItineraryItem itineraryItemCopy = itineraryItem.getCopy();
                    itineraryItemCopy.setDay(finalI);
                    itineraryItemCopy.setTrip(newTrip);
                    newTripItinerary.add(itineraryItemCopy);
                });

            }

        }

        return newTripItinerary;
    }

    public Boolean deleteTrip(String tripId) {
        Optional<ItemDetail> tripDetails = itemDetailRepository.findById(UUID.fromString(tripId));
        // Optional<ItineraryItem> itineraries = tripsRepository.findById(IdsUtils.convertStringToUUID(tripId));

        if (tripDetails.isEmpty()) {
            return Boolean.TRUE;    // Already Deleted
        }

        try {
            // tripsRepository.delete(itineraries.get());
            itemDetailRepository.deleteById(tripDetails.get().getId());
        } catch (Exception e) {
            log.debug("Failed to delete tripId: {}", tripId, e);
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private void updateProperties(ItemDetail itemDetail1, int days, CreateTripDto tripCreationRequest) throws JsonProcessingException {

        JsonNode properties=  itemDetail1.getProperties();
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(properties);
        ObjectNode node = (ObjectNode) mapper.readTree(json);

              ((ObjectNode) node.get("trip_summary")).put("duration_days", tripCreationRequest.getDuration_days());


         itemDetail1.setProperties(node);

    }
}
