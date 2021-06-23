package com.unravel.scout.service;

import com.unravel.scout.model.dto.ChangeLogItemDto;
import com.unravel.scout.model.entity.ChangeLogItem;
import com.unravel.scout.model.entity.ItineraryItem;
import com.unravel.scout.model.entity.Trip;
import com.unravel.scout.model.entity.TripDay;
import com.unravel.scout.model.enums.TypeOfAction;
import com.unravel.scout.model.exceptions.PermissionException;
import com.unravel.scout.model.request.TripChangeRequest;
import com.unravel.scout.utils.DateTimeUtils;
import com.unravel.scout.utils.Geocoder;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class TripUpdateService {

  private final ModelMapper modelMapper;
  private final TripItemService tripItemService;

  @Autowired
  public TripUpdateService(final ModelMapper modelMapper, final TripItemService tripItemService) {
    this.modelMapper = modelMapper;
    this.tripItemService = tripItemService;
  }

  // todo: needs to me Implemented
  public Optional<Trip> changeOwner(Trip trip, ChangeLogItemDto log) {
    return Optional.of(trip);
  }

  public Trip update(Trip trip, ChangeLogItemDto log) {
    Optional<Trip> optionalTrip = Optional.empty();
    switch (log.getTypeOfAction()) {
      case ADD:
        optionalTrip = addItem(trip, log);
        break;
      case REMOVE:
        optionalTrip = remove(trip, log);
        break;
      case REPLACE:
        optionalTrip = replace(trip, log);
        break;
      case SUGGEST:
        optionalTrip = suggest(trip, log);
        break;
      case CHANGE_OWNER:
        optionalTrip = changeOwner(trip, log);
        break;
      default:
        break;
    }
    return optionalTrip.orElseThrow(
        () ->
            new PermissionException(
                "User not allowed to perform " + log.getTypeOfAction() + " in this trip"));
  }

  public Trip updateTripBasicInfo(Trip trip, TripChangeRequest tripChangeRequest) {
    if (trip == null) return trip;
    else if (tripChangeRequest.getDisplayName() != null
        && !tripChangeRequest.getDisplayName().equals("")) {
      trip.setDisplayName(tripChangeRequest.getDisplayName());
    }
    if (tripChangeRequest.isJointly() != trip.isJointly()) {
      trip.setJointly(tripChangeRequest.isJointly());
    }

    trip = updateDates(trip, tripChangeRequest);
    trip = updateDuration(trip, tripChangeRequest);
    trip = updateQuestions(trip, tripChangeRequest);
    return trip;
  }

  private Trip updateDates(Trip trip, TripChangeRequest tripChangeRequest) {
    if (trip == null) return trip;
    if (tripChangeRequest.getStartDate() != null && tripChangeRequest.getEndDate() != null) {
      LocalDate startDate = DateTimeUtils.string2Date(tripChangeRequest.getEndDate());
      LocalDate endDate = DateTimeUtils.string2Date(tripChangeRequest.getStartDate());
      int duration_days = Math.abs(Days.daysBetween(endDate, startDate).getDays());
      trip.setEndDate(tripChangeRequest.getEndDate());
      trip.setStartDate(tripChangeRequest.getStartDate());
      tripChangeRequest.setDurationDays(duration_days);
    }
    return trip;
  }

  private Trip updateDuration(Trip trip, TripChangeRequest tripChangeRequest) {
    if (trip == null || tripChangeRequest.getDurationDays() == 0) return trip;
    else if (trip.getDurationDays() < tripChangeRequest.getDurationDays()) {
      expandTripByNumberOfDays(trip, tripChangeRequest.getDurationDays());
      trip.setDurationDays(tripChangeRequest.getDurationDays());
    } else if (trip.getDurationDays() > tripChangeRequest.getDurationDays()) {
      reduceTripByNumberOfDays(trip, tripChangeRequest.getDurationDays());
      trip.setDurationDays(tripChangeRequest.getDurationDays());
    }
    return trip;
  }

  private Trip reduceTripByNumberOfDays(Trip trip, int durationDays) {
    List<TripDay> additionDaysRef =
        trip.getTripDays().stream()
            .filter(f -> f.getDay() > durationDays)
            .collect(Collectors.toList());
    additionDaysRef.forEach(f -> trip.getTripDays().remove(f));
    return trip;
  }

  private Trip expandTripByNumberOfDays(Trip trip, int durationDays) {
    if (durationDays > trip.getDurationDays()) {
      List<ItineraryItem> items = tripItemService.getNewItems(trip, durationDays);
      AtomicReference<Integer> currentDayNumber = new AtomicReference<>(trip.getDurationDays() + 1);
      int startPointer = 0;
      int endPointer = 3;
      while (durationDays - trip.getDurationDays() >= 0) {
        if (endPointer > items.size() - 1 || startPointer >= items.size() - 1) break;
        AtomicReference<Integer> itemCount = new AtomicReference<>(1);
        TripDay tripDay =
            TripDay.builder()
                .day(currentDayNumber.get())
                .pace(trip.getPace())
                .itineraryItems(
                    items.subList(startPointer, endPointer).stream()
                        .peek(
                            f -> {
                              f.setDay(currentDayNumber.get());
                              f.setOrder(itemCount.get());
                              itemCount.updateAndGet(v -> v + 1);
                            })
                        .collect(Collectors.toList()))
                .build();
        trip.getTripDays().add(tripDay);
        trip.setDurationDays(trip.getDurationDays() + 1);
        currentDayNumber.getAndUpdate(v -> v + 1);
        startPointer = endPointer;
        endPointer = Math.min((items.size() - 1), (endPointer + 3));
      }
    }
    return trip;
  }

  private Trip updateQuestions(Trip trip, TripChangeRequest tripChangeRequest) {
    if (trip == null) return trip;
    if (tripChangeRequest.getQuestions() != null) {
      trip.setUserInput(tripChangeRequest.getQuestions());
    }
    return trip;
  }

  private void addItemOnAnOrder(Trip trip, ItineraryItem item, ChangeLogItemDto log) {
    trip.getTripDays()
        .forEach(
            tripDay -> {
              if (tripDay.getDay() == log.getDay()) {
                tripDay
                    .getItineraryItems()
                    .forEach(
                        f -> {
                          if (f.getOrder() == log.getOrder() - 1) {
                            item.setTransitDistance(
                                Geocoder.distance(item.getLocation(), f.getLocation(), 0.0, 0.0));
                          }
                          if (f.getOrder() == log.getOrder()) {
                            f.setTransitDistance(
                                Geocoder.distance(item.getLocation(), f.getLocation(), 0.0, 0.0));
                            f.setOrder(f.getOrder() + 1);
                          }
                          if (f.getOrder() > log.getOrder()) {
                            f.setOrder(f.getOrder() + 1);
                          }
                        });
              }
            });
  }

  private void addItemInTheEnd(Trip trip, ItineraryItem item, ChangeLogItemDto log) {
    trip.getTripDays()
        .forEach(
            tripDay -> {
              if (tripDay.getDay() == log.getDay()) {
                int i = tripDay.getItineraryItems().size() - 1;
                tripDay
                    .getItineraryItems()
                    .get(i)
                    .setTransitDistance(
                        Geocoder.distance(
                            tripDay.getItineraryItems().get(i).getLocation(),
                            item.getLocation(),
                            0.0,
                            0.0));

                if (tripDay.getItineraryItems().stream()
                    .noneMatch(f -> f.getItemId().equals(item.getItemId()))) {
                  tripDay.getItineraryItems().add(item);
                }
              }
            });
  }

  private Optional<Trip> addItem(Trip trip, ChangeLogItemDto log) {
    if (trip.isOwner(log.getUserId()) && log.getTypeOfAction().equals(TypeOfAction.ADD)) {
      ItineraryItem item = tripItemService.getItemById(log.getNewItemId());
      item.setDay(log.getDay());
      item.setOrder(log.getOrder());

      if ((log.getTargetItemId() == null)) {
        addItemInTheEnd(trip, item, log);
      } else {
        addItemOnAnOrder(trip, item, log);
      }

      return Optional.of(trip);
    } else {
      throw new PermissionException("Only Owner can perform ADD to trip");
    }
  }

  private Optional<Trip> remove(Trip trip, ChangeLogItemDto log) {
    if (trip.isOwner(log.getUserId()) && log.getTypeOfAction().equals(TypeOfAction.REMOVE)) {
      trip.getTripDays()
          .forEach(
              tripDay -> {
                if (tripDay.getDay() == log.getDay()) {
                  List<ItineraryItem> itemsToRemove =
                      tripDay.getItineraryItems().stream()
                          .filter(f -> f.getItemId().equals(log.getTargetItemId()))
                          .collect(Collectors.toList());

                  itemsToRemove.forEach(i -> tripDay.getItineraryItems().remove(i));
                }
              });
      return Optional.of(trip);
    } else {
      throw new PermissionException("Only Owner can perform remove to trip");
    }
  }

  private void replaceWithSuggestion(ItineraryItem itineraryItem, ChangeLogItemDto log) {
    // move the item to Suggestion
    ChangeLogItem changeLogItem =
        new ChangeLogItem(
            log.getUserId(),
            log.getTypeOfAction(),
            log.getStatus(),
            new ItineraryItem().swap(itineraryItem),
            log.getTimestamp());
    // swap item with changelogItem
    ItineraryItem suggestedItem =
        itineraryItem.getSuggestions().get(log.getSuggestedItemId()).getNewItem();
    suggestedItem.setDay(itineraryItem.getDay());
    suggestedItem.setOrder(itineraryItem.getOrder());
    itineraryItem.swap(suggestedItem);
    if (itineraryItem.getSuggestions() != null) {
      itineraryItem.getSuggestions().remove(log.getNewItemId());
      itineraryItem.getSuggestions().put(changeLogItem.getNewItem().getItemId(), changeLogItem);
    }
  }

  private Optional<Trip> replace(Trip trip, ChangeLogItemDto log) {
    if (trip.isOwner(log.getUserId()) && log.getTypeOfAction().equals(TypeOfAction.REPLACE)) {
      trip.getTripDays()
          .forEach(
              tripDay -> {
                if (tripDay.getDay() == log.getDay()) {
                  tripDay
                      .getItineraryItems()
                      .forEach(
                          itineraryItem -> {
                            if (itineraryItem.getItemId().equals(log.getTargetItemId())) {
                              if (log.getSuggestedItemId() != null) {
                                replaceWithSuggestion(itineraryItem, log);
                              } else if (log.getNewItemId() != null) {
                                ItineraryItem newItineraryItem =
                                    tripItemService.getItemById(log.getNewItemId());
                                newItineraryItem.setDay(itineraryItem.getDay());
                                newItineraryItem.setOrder(itineraryItem.getOrder());
                                itineraryItem.swap(newItineraryItem);
                              }
                            }
                          });
                }
              });
      return Optional.of(trip);
    } else {
      throw new PermissionException("Only Owner can perform Replace to trip");
    }
  }

  private Optional<Trip> replaceAll(Trip trip, ChangeLogItemDto log) {
    if (trip.isOwner(log.getUserId()) && log.getTypeOfAction().equals(TypeOfAction.REPLACE)) {
      trip.getTripDays()
          .forEach(
              tripDay ->
                  tripDay
                      .getItineraryItems()
                      .forEach(
                          itineraryItem -> {
                            if (itineraryItem.getItemId().equals(log.getTargetItemId())) {
                              // move the item to Suggestion
                              ChangeLogItem changeLogItem =
                                  new ChangeLogItem(
                                      log.getUserId(),
                                      log.getTypeOfAction(),
                                      log.getStatus(),
                                      new ItineraryItem().swap(itineraryItem),
                                      log.getTimestamp());

                              // swap item with changelogItem
                              itineraryItem.swap(tripItemService.getItemById(log.getNewItemId()));
                              if (itineraryItem.getSuggestions() != null) {
                                itineraryItem.getSuggestions().remove(log.getNewItemId());
                                itineraryItem
                                    .getSuggestions()
                                    .put(changeLogItem.getNewItem().getItemId(), changeLogItem);
                              }
                            }
                          }));
      return Optional.of(trip);
    } else {
      throw new PermissionException("Only Owner can perform Replace to trip");
    }
  }

  private Optional<Trip> suggest(Trip trip, ChangeLogItemDto log) {
    if (trip.canSuggest(log.getUserId()) && log.getTypeOfAction().equals(TypeOfAction.SUGGEST)) {
      trip.getTripDays()
          .forEach(
              tripDay ->
                  tripDay
                      .getItineraryItems()
                      .forEach(
                          itineraryItem -> {
                            if (itineraryItem.getItemId().equals(log.getTargetItemId())) {
                              if (itineraryItem.getSuggestions() == null) {
                                Map<String, ChangeLogItem> suggestion = new HashMap<>();
                                suggestion.put(
                                    log.getNewItemId(), modelMapper.map(log, ChangeLogItem.class));
                                itineraryItem.setSuggestions(suggestion);
                              } else {
                                itineraryItem
                                    .getSuggestions()
                                    .put(
                                        log.getNewItemId(),
                                        modelMapper.map(log, ChangeLogItem.class));
                              }
                            }
                          }));
      return Optional.of(trip);
    } else {
      return Optional.empty();
    }
  }
}
