package com.unravel.scout.service;

import com.unravel.scout.model.entity.Trip;
import com.unravel.scout.model.entity.User;
import com.unravel.scout.model.enums.DocumentType;
import com.unravel.scout.model.exceptions.ResourceNotFoundException;
import com.unravel.scout.repository.UserRepository;
import com.unravel.scout.utils.DateTimeUtils;
import com.unravel.scout.utils.IdsUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Value("${scout.destinations.document._id}")
  private String DESTINATION_ID;

  @Autowired
  public UserService(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getOrCreateUserDocument(String userId) {
    try {
      return getUserDocument(userId);
    } catch (ResourceNotFoundException exception) {
      return createUserDocument(userId);
    }
  }

  private User createUserDocument(String userId) {
    List<String> suggested =
        new ArrayList<String>() {
          {
            add("101");
            add("102");
            add("103");
          }
        };
    User user =
        new User(
            IdsUtils.getIdWithPrefix(userId, DocumentType.USER),
            userId,
            null,
            null,
            suggested,
            null);
    return userRepository.save(user);
  }

  public User getUserDocument(String userId) {
    return userRepository
        .findBy_id(IdsUtils.getIdWithPrefix(userId, DocumentType.USER))
        .orElseThrow(() -> new ResourceNotFoundException("User"));
  }

  public List<String> getUsersTripIds(String userId) {
    return getUserDocument(userId).getMyItineraries();
  }

  public Map<String, String> getUsersRecentlyViewedTripIds(String userId) {
    return getUserDocument(userId).getRecentlyViewed();
  }

  public List<String> getSharedTripIds(String userId) {
    return getUserDocument(userId).getSharedWithMe();
  }

  public List<String> getSuggestedTripIds(String userId) {
    return getUserDocument(userId).getSuggested();
  }

  public void addToOwnedTrip(String userId, Trip trip) {
    addOwnedTripIds(userId, trip.getId());
  }

  public void shareItinerary(String userId, Trip trip) {
    addSharedTripIds(userId, trip.getId());
  }

  public void removeFromOwnedTrip(String userId, Trip trip) {
    User user = getOrCreateUserDocument(userId);
    user.getMyItineraries().remove(trip.getId());
    userRepository.save(user);
  }

  public void removeFromSharedTrip(String userId, Trip trip) {
    User user = getOrCreateUserDocument(userId);
    user.getSharedWithMe().remove(trip.getId());
    userRepository.save(user);
  }

  public User addOwnedTripIds(String userId, String itrId) {
    User u = getOrCreateUserDocument(userId);
    if (u.getMyItineraries() == null) {
      u.setMyItineraries(new ArrayList<>());
    }

    u.getMyItineraries().add(itrId);
    return userRepository.save(u);
  }

  private void addSharedTripIds(String userId, String itrId) {
    User u = getOrCreateUserDocument(userId);
    if (u.getSharedWithMe() == null) {
      u.setSharedWithMe(new ArrayList<>());
    }

    if (!u.getSharedWithMe().contains(itrId)) {
      u.getSharedWithMe().add(itrId);
      userRepository.save(u);
    }
  }

  public User addRecentlyViewedTripIds(String userId, String itrId, DateTime timestamp) {
    User u = getOrCreateUserDocument(userId);
    if (u.getRecentlyViewed() == null) {
      u.setRecentlyViewed(new HashMap<>());
    }
    if (u.getRecentlyViewed().size() >= 5) {
      String oldestItem =
          u.getRecentlyViewed().entrySet().stream()
              .max(
                  (Map.Entry<String, String> entry1, Map.Entry<String, String> entry2) -> {
                    if (DateTimeUtils.string2DateTime(entry1.getValue())
                        .isAfter(DateTimeUtils.string2DateTime(entry2.getValue()))) return 1;
                    return -1;
                  })
              .get()
              .getKey();

      u.getRecentlyViewed().remove(oldestItem);
    }
    u.getRecentlyViewed().put(itrId, DateTimeUtils.dateTime2String(timestamp));
    return userRepository.save(u);
  }

  public User deleteMyTrip(String userId, String tripId) {
    User u = getOrCreateUserDocument(userId);
    u.getMyItineraries().remove(tripId);
    return userRepository.save(u);
  }

  public User deleteSharedWithMe(String userId, String tripId) {
    User u = getOrCreateUserDocument(userId);
    u.getSharedWithMe().remove(tripId);
    return userRepository.save(u);
  }
}
