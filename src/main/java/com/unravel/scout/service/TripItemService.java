package com.unravel.scout.service;

import com.unravel.scout.model.entity.Geocode;
import com.unravel.scout.model.entity.ItineraryItem;
import com.unravel.scout.model.entity.Trip;
import com.unravel.scout.model.request.LocationQuery;
import com.unravel.scout.model.request.TripItemRequest;
import com.unravel.scout.model.response.TripItemsResponse;
import com.unravel.scout.model.response.TripItemsSuggestionResponse;
import com.unravel.scout.utils.JsonUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TripItemService {

  private final RestClient restClient;

  @Value("${trip.item.suggestion.url}")
  private String SUGGESTION_URL;

  @Value("${trip.item.by.id.url}")
  private String ITEM_ID_URL;

  @Autowired
  public TripItemService(final RestClient restClient) {
    this.restClient = restClient;
  }

  public ItineraryItem getItemById(String itemId) {
    try {
      log.debug(String.format("ITEM_ID %s", itemId));
      String resString = restClient.get(String.format("%s/%s", ITEM_ID_URL, itemId));
      TripItemsResponse tripItemResponse = JsonUtils.getObject(resString, TripItemsResponse.class);
      return (tripItemResponse.getStatus() == 1) ? tripItemResponse.getData() : null;
    } catch (Exception e) {
      log.error(e.getLocalizedMessage());
      return null;
    }
  }

  private List<ItineraryItem> getNewItems(List<Geocode> locations, int responseSize, String city) {
    try {
      String tripItemRequest =
          JsonUtils.getString(
              TripItemRequest.builder()
                  .query(
                      LocationQuery.builder()
                          .size(responseSize)
                          .city(city)
                          .locations(locations)
                          .build())
                  .build());
      log.debug(tripItemRequest);
      String resString = restClient.post(SUGGESTION_URL, tripItemRequest);
      TripItemsSuggestionResponse tripItemResponse =
          JsonUtils.getObject(resString, TripItemsSuggestionResponse.class);
      return (tripItemResponse.getStatus() == 1) ? tripItemResponse.getData() : new ArrayList<>();
    } catch (Exception e) {
      log.error(e.getLocalizedMessage());
      return new ArrayList<>();
    }
  }

  public List<ItineraryItem> getNewItems(Trip trip, int dayNumber) {
    if (dayNumber >= trip.getDurationDays()) {
      List<Geocode> locations =
          trip.getTripDays().stream()
              .flatMap(m -> m.getItineraryItems().stream().map(ItineraryItem::getLocation))
              .collect(Collectors.toList());
      return getNewItems(
          locations, (dayNumber - trip.getDurationDays()) * 3, trip.getAddress().getCity());
    }
    return new ArrayList<>();
  }
}
