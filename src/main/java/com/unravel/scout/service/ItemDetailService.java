package com.unravel.scout.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unravel.constants.UnravelConstants;
import com.unravel.scout.model.constants.ApiResponseMessages;
import com.unravel.scout.model.dto.v1.ComponentDto;
import com.unravel.scout.model.dto.v1.GetRelatedTripsRequest;
import com.unravel.scout.model.dto.v1.ItemDetailDto;
import com.unravel.scout.model.dto.v1.RelatedUserTripDto;
import com.unravel.scout.model.entity.v1.ItemComponent;
import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.RecentlyViewed;
import com.unravel.scout.repositories.ComponentRepository;
import com.unravel.scout.repositories.ItemDetailRepository;
import com.unravel.scout.repositories.RecentlyViewedRepository;
import com.unravel.scout.repositories.TripsRepository;
import com.unravel.universal.UnravelResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ItemDetailService {

    private final ItemDetailRepository itemDetailRepository;
    private final ComponentRepository componentRepository;
    private final RecentlyViewedRepository recentlyViewedRepository;
    private final TripsRepository tripsRepository;
    private final ObjectMapper objectMapper;

    public ItemDetailService(ItemDetailRepository itemDetailRepository,
                             ComponentRepository componentRepository,
                             RecentlyViewedRepository recentlyViewedRepository,
                             TripsRepository tripsRepository) {
        this.itemDetailRepository = itemDetailRepository;
        this.componentRepository = componentRepository;
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.tripsRepository = tripsRepository;
        this.objectMapper = new ObjectMapper();
    }

    public UnravelResponse getItemById(String itemId, String userId) {
        UnravelResponse response = new UnravelResponse();
        Optional<ItemDetail> item = this.itemDetailRepository.findById(UUID.fromString(itemId));

        if (item.isEmpty()) {
            log.debug("No item found for id: {} userId: {}", itemId, userId);
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("No Record found");
            return response;
        }

        ItemDetailDto itemDetailDto = ItemDetailDto.mapToItemDetailDto(item.get());

        response.setMessage("Success");
        response.setStatus(UnravelConstants.TRUE);
        response.setData(itemDetailDto);

        // make entry into recently viewed
        log.debug("Making entry for itemId: {} userId:{} into recently_viewed.", itemId, userId);
        RecentlyViewed view = new RecentlyViewed();
        view.setUserId(userId);
        view.setItem(item.get());
        recentlyViewedRepository.save(view);

        return response;
    }

    public UnravelResponse getItemComponentsByName(String itemId, String userId, String componentName) {
        UnravelResponse response = new UnravelResponse();

        List<ItemComponent> components = componentRepository
                .findComponentsByNameAndItemId(componentName, UUID.fromString(itemId));
        List<ComponentDto> componentDtos = components.stream()
                .map(ComponentDto::mapToComponentDto)
                .collect(Collectors.toList());

        if (components.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("No Record found");
            return response;
        }
        response.setMessage("Success");
        response.setStatus(UnravelConstants.TRUE);
        response.setData(componentDtos.get(0));     // as there is only one component of each type for one item

        return response;
    }

    public UnravelResponse getSpotDetails(String itemId, String userId) {
        UnravelResponse response = new UnravelResponse();
        Optional<ItemDetail> item = this.itemDetailRepository.findById(UUID.fromString(itemId));

        if (item.isEmpty()) {
            log.debug("No item found for id: {} userId: {}", itemId, userId);
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("No Record found");
            return response;
        }

        ItemDetailDto itemDetailDto = ItemDetailDto.mapToItemDetailDtoBasic(item.get());

        response.setMessage("Success");
        response.setStatus(UnravelConstants.TRUE);
        response.setData(itemDetailDto);

        // make entry into recently viewed
        log.debug("Making entry for itemId: {} userId:{} into recently_viewed.", itemId, userId);
        RecentlyViewed view = new RecentlyViewed();
        view.setUserId(userId);
        view.setItem(item.get());
        recentlyViewedRepository.save(view);

        return response;
    }

    public UnravelResponse getRelatedTrips(GetRelatedTripsRequest request) {
        UnravelResponse response = new UnravelResponse();

        String userId = request.getUserId();
        List<UUID> itemIds = request.getItemIds().stream().map(UUID::fromString).collect(Collectors.toList());
        List<ItemDetail> tripDetails = tripsRepository.findByItemIdInCuratedTrips(itemIds);

        var result = tripDetails.stream()
            .map(ItemDetailDto::mapToItemDetailDtoBasicOnly)
            .collect(Collectors.toList());

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_FETCHED);
        response.setData(result);

        return response;
    }

    public UnravelResponse getRelatedUserTrips(String userId, String itemId) {
        UnravelResponse response = new UnravelResponse();

        List<ItemDetail> userTripDetails = tripsRepository.findUserTrips(Long.parseLong(userId));
        List<Object[]> tripsWithItemList = tripsRepository
            .findUserTripsByItemId(Long.parseLong(userId), UUID.fromString(itemId));

        if (userTripDetails.isEmpty() || tripsWithItemList.isEmpty()) {
            response.setStatus(UnravelConstants.FALSE);
            response.setMessage("No Record found");
            return response;
        }

        Map<String, Boolean> tripsWithItem = tripsWithItemList.stream()
            .collect(Collectors.toMap(row -> (String)row[0], row -> true));

        var result = userTripDetails.stream()
            .map(item -> {
                Boolean hasItem = false;
                String tripId = item.getId().toString();
                if (tripsWithItem.containsKey(tripId) && tripsWithItem.get(tripId)) {
                    hasItem = tripsWithItem.get(tripId);
                }
                return RelatedUserTripDto.mapToRelatedUserTripDto(item, hasItem);
            }).collect(Collectors.toList());

        response.setStatus(UnravelConstants.TRUE);
        response.setMessage(ApiResponseMessages.ITEM_FETCHED);
        response.setData(result);

        return response;
    }
}
