package com.unravel.scout.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unravel.constants.UnravelConstants;
import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.v1.*;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.ItineraryItem;
import com.unravel.scout.repositories.ItemDetailRepository;
import com.unravel.scout.repositories.TripsRepository;
import com.unravel.scout.utils.Constants;
import com.unravel.scout.utils.DateTimeUtils;
import com.unravel.universal.UnravelResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Service
public class TripItineraryService {

    private final TripsRepository tripsRepository;
    private final ItemDetailRepository itemDetailRepository;
    @Value("${map.plot.radius}")
    private double distance;
    @Autowired
    public TripItineraryService(TripsRepository tripsRepository,
                                ItemDetailRepository itemDetailRepository) {
        this.tripsRepository = tripsRepository;
        this.itemDetailRepository = itemDetailRepository;
    }

    public UnravelResponse getTripItinerary(String tripId, String userId) {
        UnravelResponse response = new UnravelResponse();

        Optional<ItemDetail> tripDetails = this.itemDetailRepository.findById(UUID.fromString(tripId));

        if(tripDetails.isEmpty() /*|| tripDetails.get().getTripDays().isEmpty()*/) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }

        ItemDetail trip = tripDetails.get();

        response.setMessage("Success");
        response.setStatus(UnravelConstants.TRUE);
        response.setData(mapToTripDto(trip));
        return response;
    }

    @Transactional
    protected ItemDetail updateInDb(ItemDetail trip) {
        return itemDetailRepository.save(trip);
    }

    private ItemDetailDto mapToTripDto(ItemDetail trip) {
        ItemDetailDto tripDto = ItemDetailDto.mapToItemDetailDtoBasicOnly(trip);

        TreeMap<Integer, List<ItineraryItemDto>> itineraryItemsByDay = trip.getTripDays().stream()
            .map(ItineraryItemDto::mapToItineraryItemDto)
            .collect(
                Collectors.groupingBy(ItineraryItemDto::getDay, TreeMap::new, Collectors.toList())
            );

        // sort the day-wise itinerary-items also and change to day-wise order
        itineraryItemsByDay.entrySet().forEach(integerListEntry -> {
            AtomicInteger counter = new AtomicInteger(1);
            List<ItineraryItemDto> sortedItems = integerListEntry.getValue().stream()
                .filter(i -> i.getOrder() != 0) // empty list for empty day
                .sorted(Comparator.comparingInt(ItineraryItemDto::getOrder))
                .peek(i -> i.setOrder(counter.getAndIncrement()))
                .collect(Collectors.toList());
            integerListEntry.setValue(sortedItems);
        });

        List<TripDayDto> tripDays = itineraryItemsByDay.entrySet().stream()
            .map(e -> TripDayDto.getTripDayDto(e.getKey(), e.getValue()))
            .collect(Collectors.toList());

        tripDto.setTripDays(tripDays);

        return tripDto;
    }

    public UnravelResponse addNewDayToTrip(AddNewDayToTripRequest request) {
        UnravelResponse response = new UnravelResponse();

        String tripId = request.getTripId();
        String userId = request.getUserId();

        var tripDetail = itemDetailRepository
            .findByIdAndUserId(UUID.fromString(tripId), Long.parseLong(userId));

        if (tripDetail.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }
        if (tripDetail.get().getIsDefault()) {  // => curated trip
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("Curated Trip cannot be Modified");
            return response;
        }

        ItemDetail trip = tripDetail.get();
        Set<ItineraryItem> itineraryItems = trip.getTripDays();

        TreeMap<Integer, List<ItineraryItem>> itineraryItemsByDay = itineraryItems.stream()
            .collect(Collectors.groupingBy(ItineraryItem::getDay, TreeMap::new, Collectors.toList()));

        int maxDay = itineraryItemsByDay.lastKey();
        int newMaxDay = maxDay + 1;

        // create an ItineraryItem for an empty day i.e. order=0
        ItineraryItem emptyDayItinerary = new ItineraryItem();
        emptyDayItinerary.setTrip(trip);
        emptyDayItinerary.setDay(newMaxDay);
        emptyDayItinerary.setOrder(0);
        // emptyDayItinerary.setItem(null);    // no items for empty day

        trip.getTripDays().add(emptyDayItinerary);

        ItemDetail updatedTrip = null;

        try {
            updatedTrip = updateInDb(trip);
        } catch (Exception e) {
            // TODO: log and respond with error
            e.printStackTrace();
        }

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_UPDATED);
        response.setData(mapToTripDto(updatedTrip));

        return response;
    }

    public UnravelResponse removeDayFromTrip(RemoveADayFromTripRequest request) {
        UnravelResponse response = new UnravelResponse();

        String tripId = request.getTripId();
        String userId = request.getUserId();
        var tripDetail = itemDetailRepository
            .findByIdAndUserId(UUID.fromString(tripId), Long.parseLong(userId));

        if (tripDetail.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }
        if (tripDetail.get().getIsDefault()) {  // => curated trip
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("Curated Trip cannot be Modified");
            return response;
        }

        ItemDetail trip = tripDetail.get();
        Set<ItineraryItem> itineraryItems = trip.getTripDays();

        TreeMap<Integer, List<ItineraryItem>> itineraryItemsByDay = itineraryItems.stream()
            .collect(Collectors.groupingBy(ItineraryItem::getDay, TreeMap::new, Collectors.toList()));

        int dayToRemove = request.getDay();

        itineraryItemsByDay.get(dayToRemove).forEach(itineraryItems::remove);

        if (dayToRemove < itineraryItemsByDay.lastKey()) {      // => re-ordering needed
            NavigableMap<Integer, List<ItineraryItem>> toReorderItemsByDay =
                itineraryItemsByDay.tailMap(dayToRemove, false);
            toReorderItemsByDay.forEach((key, value) -> {
                value.forEach(i -> {
                    Integer currentDay  = i.getDay();
                    i.setDay(currentDay - 1);
                });
            });
        }

        ItemDetail updatedTrip = null;

        try {
            updatedTrip = updateInDb(trip);
        } catch (Exception e) {
            // TODO: log and respond with error
            e.printStackTrace();
        }

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_UPDATED);
        response.setData(mapToTripDto(updatedTrip));

        return response;
    }

    public UnravelResponse addItemToDay(AddItemToDayRequest request) {
        UnravelResponse response = new UnravelResponse();

        String tripId = request.getTripId();
        String userId = request.getUserId();
        var tripDetail = itemDetailRepository
            .findByIdAndUserId(UUID.fromString(tripId), Long.parseLong(userId));
        var itemToAddDetail = itemDetailRepository.findById(UUID.fromString(request.getItemId()));

        if (tripDetail.isEmpty() || itemToAddDetail.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }
        if (tripDetail.get().getIsDefault()) {  // => curated trip
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("Curated Trip cannot be Modified");
            return response;
        }

        var existingItem = tripsRepository  // TODO: item or itinerary_item?
            .findByTrip_IdAndItem_Id(UUID.fromString(request.getTripId()), UUID.fromString(request.getItemId()));
        if (existingItem.isPresent()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.DUPLICATE_ITEM);
            return response;
        }

        ItemDetail trip = tripDetail.get();
        Set<ItineraryItem> itineraryItems = trip.getTripDays();

        int day = request.getDay();
        ItemDetail itemToAdd = itemToAddDetail.get();
        int currentMaxOrderInDay = itineraryItems.stream()
            .filter(i -> i.getDay().equals(day))
            .mapToInt(ItineraryItem::getOrder)
            .max()
            .orElseThrow(NoSuchFieldError::new);

        if (currentMaxOrderInDay == 0) {
            // If item is being added to empty day then remove the order=0 entry
            itineraryItems.removeIf(itineraryItem -> itineraryItem.getDay().equals(day));
        }

        String duration = Constants.DEFAULT_DURATION;
        JsonNode durationNode = itemToAdd.getProperties().get("duration_min");
        if (durationNode != null && !durationNode.isEmpty()) {
            duration = durationNode.textValue();
        }

        ItineraryItem newItineraryItem = ItineraryItem.builder()    // TODO: how to get other properties
            .trip(trip)
            .day(day)
            .order(currentMaxOrderInDay + 1)
            .item(itemToAdd)
            // .pace()
            // .startTime()
            // .endTime()
            .durationMin(Integer.parseInt(duration))
            // .transitDurationMin()
            .build();

        trip.getTripDays().add(newItineraryItem);

        ItemDetail updatedTrip = null;

        try {
            updatedTrip = updateInDb(trip);
        } catch (Exception e) {
            // TODO: log and respond with error
            e.printStackTrace();
        }

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_UPDATED);
        response.setData(mapToTripDto(updatedTrip));

        return response;
    }

    public UnravelResponse removeItemFromDay(RemoveItemFromDayRequest request) {
        UnravelResponse response = new UnravelResponse();

        String tripId = request.getTripId();
        String userId = request.getUserId();
        var tripDetail = itemDetailRepository
            .findByIdAndUserId(UUID.fromString(tripId), Long.parseLong(userId));

        if (tripDetail.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }
        if (tripDetail.get().getIsDefault()) {  // => curated trip
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("Curated Trip cannot be Modified");
            return response;
        }

        ItemDetail trip = tripDetail.get();
        Set<ItineraryItem> itineraryItems = trip.getTripDays();

        TreeMap<Integer, List<ItineraryItem>> itineraryItemsByDay = itineraryItems.stream()
            .collect(Collectors.groupingBy(ItineraryItem::getDay, TreeMap::new, Collectors.toList()));

        int day = request.getDay();
        // int order = request.getOrder();
        UUID idToRemove = UUID.fromString(request.getItemId());    // TODO: change to itemId

        List<ItineraryItem> dayItineraryItems = itineraryItemsByDay.get(day);
        dayItineraryItems.sort(Comparator.comparingInt(ItineraryItem::getOrder));

        boolean isRemoved = false;
        for (var itineraryItem: dayItineraryItems) {
            if (isRemoved) {
                int currOrder = itineraryItem.getOrder();
                itineraryItem.setOrder(currOrder - 1);
            }
            if (itineraryItem.getItem().getId().equals(idToRemove) /*&& order == itineraryItem.getOrder()*/) {
                trip.getTripDays().remove(itineraryItem);
                isRemoved = true;
            }

        }

        ItemDetail updatedTrip = null;
        try {
            updatedTrip = updateInDb(trip);
        } catch (Exception e) {
            // TODO: log and respond with error
            e.printStackTrace();
        }

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_UPDATED);
        response.setData(mapToTripDto(updatedTrip));

        return response;
    }

    public UnravelResponse replaceItemInDay(ReplaceItemInDayRequest request) {
        UnravelResponse response = new UnravelResponse();

        String tripId = request.getTripId();
        String userId = request.getUserId();
        var tripDetail = itemDetailRepository
            .findByIdAndUserId(UUID.fromString(tripId), Long.parseLong(userId));
        var itemToAddDetail = itemDetailRepository.findById(UUID.fromString(request.getNewItemId()));

        if (tripDetail.isEmpty() || itemToAddDetail.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }
        if (tripDetail.get().getIsDefault()) {  // => curated trip
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("Curated Trip cannot be Modified");
            return response;
        }

        var existingItem = tripsRepository  // TODO: item or itinerary_item?
            .findByTrip_IdAndItem_Id(UUID.fromString(request.getTripId()), UUID.fromString(request.getNewItemId()));
        if (existingItem.isPresent()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.DUPLICATE_ITEM);
            return response;
        }

        ItemDetail trip = tripDetail.get();
        Set<ItineraryItem> itineraryItems = trip.getTripDays();

        int day = request.getDay();
        // int order = request.getOrder();
        ItemDetail itemToAdd = itemToAddDetail.get();

        var dayItineraryItems = itineraryItems.stream()
            .filter(i -> i.getDay() == day)
            .sorted(Comparator.comparingInt(ItineraryItem::getOrder))
            .collect(Collectors.toList());

        var itineraryItemToRemove = dayItineraryItems.stream()
            // .filter(i -> i.getDay() == day)
            // .filter(i -> i.getOrder() == order)
            .filter(i -> i.getItem().getId().equals(UUID.fromString(request.getOldItemId())))
            .collect(Collectors.toList());

        if (itineraryItemToRemove.isEmpty()) {
            // TODO: throw error that no itinerary item with given id exists in the day exists so can't be removed
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }

        int order = itineraryItemToRemove.get(0).getOrder();
        // remove old item
        trip.getTripDays().remove(itineraryItemToRemove.get(0));    // TODO: ensure that it doesn't cascade

        String duration = Constants.DEFAULT_DURATION;
        JsonNode durationNode = itemToAdd.getProperties().get("duration_min");
        if (durationNode != null && !durationNode.isEmpty()) {
            duration = durationNode.textValue();
        }

        // create new item
        ItineraryItem newItineraryItem = ItineraryItem.builder()    // TODO: how to get other properties
            .trip(trip)
            .day(day)
            .order(order)
            .item(itemToAdd)
            // .pace()
            // .startTime()
            // .endTime()
            .durationMin(Integer.parseInt(duration))
            // .transitDurationMin()
            .build();

        // add new item
        trip.getTripDays().add(newItineraryItem);

        ItemDetail updatedTrip = null;

        try {
            updatedTrip = updateInDb(trip);
        } catch (Exception e) {
            // TODO: log and respond with error
            e.printStackTrace();
        }

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_UPDATED);
        response.setData(mapToTripDto(updatedTrip));

        return response;
    }

    public UnravelResponse updateTripDates(UpdateTripDatesRequest request) {
        UnravelResponse response = new UnravelResponse();

        String tripId = request.getTripId();
        String userId = request.getUserId();
        var tripDetail = itemDetailRepository
            .findByIdAndUserId(UUID.fromString(tripId), Long.parseLong(userId));

        if (tripDetail.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }

        ItemDetail trip = tripDetail.get();

        String startDateStr = request.getStartDate();
        String endDateStr = request.getEndDate();
        LocalDateTime startDate = DateTimeUtils.strToLocalDateTime(startDateStr);
        LocalDateTime endDate = DateTimeUtils.strToLocalDateTime(endDateStr);

        JsonNode properties = trip.getProperties();
        JsonNode tripSummary = properties.get("trip_summary");
        if (tripSummary != null && !tripSummary.isNull()) {
            ((ObjectNode)tripSummary).put("start_date", startDateStr);
            ((ObjectNode)tripSummary).put("end_date", endDateStr);
        }

        long days = ChronoUnit.DAYS.between(startDate, endDate);

        updatedItineraryByDays(Math.toIntExact(days), trip);

        ItemDetail updatedTrip = null;

        try {
            updatedTrip = updateInDb(trip);
        } catch (Exception e) {
            // TODO: log and respond with error
            e.printStackTrace();
        }

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_UPDATED);
        response.setData(mapToTripDto(updatedTrip));

        return response;
    }

    public UnravelResponse updateTripDays(UpdateTripDaysRequest request) {
        UnravelResponse response = new UnravelResponse();

        String tripId = request.getTripId();
        String userId = request.getUserId();
        var tripDetail = itemDetailRepository
            .findByIdAndUserId(UUID.fromString(tripId), Long.parseLong(userId));

        if (tripDetail.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }

        int requiredNoOfDays = request.getDays();
        ItemDetail trip = tripDetail.get();

        updatedItineraryByDays(requiredNoOfDays, trip);

        JsonNode properties = trip.getProperties();
        JsonNode tripSummary = properties.get("trip_summary");
        if (tripSummary != null && !tripSummary.isNull()) {
            ((ObjectNode) tripSummary).put("duration_days", requiredNoOfDays);
        }

        ItemDetail updatedTrip = null;

        try {
            updatedTrip = updateInDb(trip);
        } catch (Exception e) {
            // TODO: log and respond with error
            e.printStackTrace();
        }

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_UPDATED);
        response.setData(mapToTripDto(updatedTrip));

        return response;
    }

    /**
     * Modifies the itinerary to required number of days.
     * If requiredNoOfDays > no. of days in trip, then add more days.
     * If requiredNoOfDays < no. of days in trip, then remove extra days
     * @param requiredNoOfDays
     * @param trip
     */
    private void updatedItineraryByDays(int requiredNoOfDays, ItemDetail trip) {
        Set<ItineraryItem> itineraryItems = trip.getTripDays();

        TreeMap<Integer, List<ItineraryItem>> itineraryItemsByDay = itineraryItems.stream()
            .collect(
                Collectors.groupingBy(ItineraryItem::getDay, TreeMap::new, Collectors.toList())
            );
        int existingNoOfDays = itineraryItemsByDay.size();

        // if existingNoOfDays == days => No Change
        if (requiredNoOfDays < existingNoOfDays) {
            // remove extra days
            itineraryItemsByDay.tailMap(requiredNoOfDays, false).forEach((key, val) -> {
                val.forEach(itineraryItems::remove);
            });
        } else if (requiredNoOfDays > existingNoOfDays){
            // add extra days
            IntStream.range(existingNoOfDays + 1, requiredNoOfDays + 1).forEach(day -> {
                // create an ItineraryItem for an empty day i.e. order=0
                ItineraryItem emptyDayItinerary = new ItineraryItem();
                emptyDayItinerary.setTrip(trip);
                emptyDayItinerary.setDay(day);
                emptyDayItinerary.setOrder(0);

                trip.getTripDays().add(emptyDayItinerary);
            });
        }
    }

    public UnravelResponse reorderTripItems(ReorderTripItemsRequest request) {
        UnravelResponse response = new UnravelResponse();
        String errorMsg = "";

        String tripId = request.getTripId();
        String userId = request.getUserId();
        var tripDetail = itemDetailRepository
            .findByIdAndUserId(UUID.fromString(tripId), Long.parseLong(userId));

        if (tripDetail.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }

        ItemDetail trip = tripDetail.get();
        Set<ItineraryItem> itineraryItems = trip.getTripDays();

        errorMsg = validateReordering(request, errorMsg, itineraryItems);
        if (!errorMsg.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(errorMsg);
            return response;
        }

        Map<Integer, ItineraryItem> emptyDayItineraryMap = itineraryItems.stream()
            .filter(i -> i.getItem() == null)
            .collect(Collectors.toMap(ItineraryItem::getDay, i -> i));

        Map<String, ItineraryItem> itemIdItineraryItemMap = itineraryItems.stream()
            .filter(i -> i.getItem() != null)
            .collect(Collectors.toMap(i -> i.getItem().getId().toString(), i -> i));

        List<Integer> emptyDaysInRequest = new ArrayList<>();

        for (var reorderDto: request.getItemsToReorder()) {
            String itemId = reorderDto.getItemId();
            if (itemId == null && reorderDto.getOrder() == 0) {
                emptyDaysInRequest.add(reorderDto.getDay());
                continue;
            }
            ItineraryItem itineraryItem = itemIdItineraryItemMap.get(itemId);
            itineraryItem.setDay(reorderDto.getDay());
            itineraryItem.setOrder(reorderDto.getOrder());
        }

        var mapIterator = emptyDayItineraryMap.entrySet().iterator();
        var listIterator = emptyDaysInRequest.listIterator();

        while (mapIterator.hasNext() && listIterator.hasNext()) {
            int newDay = listIterator.next();
            var entry = mapIterator.next();
            ItineraryItem itineraryItem = entry.getValue();
            itineraryItem.setDay(newDay);
            itineraryItem.setOrder(0);
        }

        if (mapIterator.hasNext()) {    // if extra empty days remaining in old order
            while (mapIterator.hasNext()) {
                var entry = mapIterator.next();
                ItineraryItem itineraryItem = entry.getValue();
                itineraryItems.remove(itineraryItem);
            }
        }

        if (listIterator.hasNext()) {   // if more new empty days are required in new order
            while (listIterator.hasNext()) {
                // create an ItineraryItem for an empty day
                int newDay = listIterator.next();
                ItineraryItem emptyDayItinerary = new ItineraryItem();
                emptyDayItinerary.setTrip(trip);
                emptyDayItinerary.setDay(newDay);
                emptyDayItinerary.setOrder(0);

                itineraryItems.add(emptyDayItinerary);
            }
        }

        ItemDetail updatedTrip = null;

        try {
            updatedTrip = updateInDb(trip);
        } catch (Exception e) {
            // TODO: log and respond with error
            e.printStackTrace();
        }

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_UPDATED);
        response.setData(mapToTripDto(updatedTrip));

        return response;
    }

    private String validateReordering(ReorderTripItemsRequest request, String errorMsg,
                                      Set<ItineraryItem> itineraryItems) {
        // validate ItemId set mismatch
        Set<String> existingItemIds = itineraryItems.stream()
            .filter(i -> i.getItem() != null)
            .map(i -> i.getItem().getId().toString())
            .collect(Collectors.toSet());

        Set<String> requestItemIds = request.getItemsToReorder().stream()
            .map(ReorderDto::getItemId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        if (!existingItemIds.equals(requestItemIds)) {
            errorMsg += "Items in request don't match with items in Trip. \n";
        }
        // validate unusual day/order numbers
        int existingNoOfItems = existingItemIds.size();
        int requestNoOfItems = requestItemIds.size();
        if (requestNoOfItems != existingNoOfItems) {
            errorMsg += "No. of items do not match with no. of items in Trip. \n";
        }

        if (!errorMsg.isEmpty()) {
            log.debug("Validation failed for TripReorder request. " +
                ">error_message: {} >existingItemIds: {}", errorMsg, existingItemIds);
        }

        return errorMsg;
    }

    public UnravelResponse fetchItemsByItineraryDays(Long userId, UUID tripId, int day) {
        UnravelResponse response = new UnravelResponse();
        Map<String, Object> responseMap = new HashMap<>();

        Optional<ItemDetail> itemDetails = itemDetailRepository.findByIdAndUserId(tripId, userId);
        if (itemDetails == null || !itemDetails.isPresent()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage(ApiResponseMessages.ITEM_NOT_FOUND);
            return response;
        }
        List<ItineraryItem> itineraryItems = tripsRepository.findByTripIdAndDay(tripId, day);
        List<ItineraryLocationDto> locationsDtoList = itineraryItems.stream().map(ItineraryLocationDto::mapToItineraryItemToLocationDto).collect(Collectors.toList());

        if (locationsDtoList.isEmpty()) {
            log.debug("No item found for Itinerary day {} ", day + "  And Trip ID" + tripId);
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("No Record found");
            responseMap.put("near_attractions", locationsDtoList);
        } else {
            Map<String, Double> resultCenter = ItineraryLocationDto.calculateCenterWithMutipleLocation(locationsDtoList);
            List<Object[]> finalResponse = tripsRepository.findByTripIdAndItineraryDay(resultCenter.get("latitude"), resultCenter.get("longitude"), distance);
            response.setMessage("Success");
            response.setStatus(UnravelConstants.TRUE);
            responseMap.put("near_attractions", ItineraryLocationDto.mapToCustomResponseToLocationDto(finalResponse));

        }
        responseMap.put("itinerary_day", day);
        responseMap.put("attractions", locationsDtoList);

        response.setData(responseMap);
        return response;

    }


}
