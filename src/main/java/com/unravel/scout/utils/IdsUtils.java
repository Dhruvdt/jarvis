package com.unravel.scout.utils;

import com.unravel.scout.model.enums.DocumentType;
import com.unravel.scout.model.exceptions.NotValidException;

import java.util.UUID;

public class IdsUtils {

  public static String randomId() {
    return UUID.randomUUID().toString();
  }

  public static String randomTripId() {
    return DocumentType.TRIP.getIdPrefix() + randomId();
  }

  public static String randomUserId() {
    return DocumentType.USER.getIdPrefix() + '-' + randomId();
  }

  public static String randomChangeLogId() {
    return DocumentType.CHANGE_LOG.getIdPrefix() + randomId();
  }

  public static String randomTripItemId() {
    return DocumentType.TRIP_ITEM.getIdPrefix() + randomId();
  }

  public static String changeLogId2TripId(String id) {
    return id.replace("chl", "itr");
  }

  public static String tripId2ChangeLogId(String id) {
    return id.replace("itr", "chl");
  }

  public static String getIdWithPrefix(String id, DocumentType targetType) {
    String originalId = targetType.getIdPrefix() + "-";
    if (id.contains(targetType.getIdPrefix())) {
      return id;
    } else if (id.contains("-")) {
      String[] idSplits = id.split("-");
      if (DocumentType.getValueForPrefix(idSplits[0]) == null) {
        return targetType.getIdPrefix() + '-' + id;
      } else if (DocumentType.isSameFamily(idSplits[0], targetType.getIdPrefix())) {
        id = id.replace(idSplits[0], targetType.getIdPrefix());
        return id;
      } else {
        throw new NotValidException("Document Family");
      }
    } else {
      return targetType.getIdPrefix() + '-' + id;
    }
  }

  public static String removeIdPrefix(String id, DocumentType targetType) {
    String originalId = targetType.getIdPrefix() + "-";
    return (id.contains(originalId)) ? id.replace(originalId, "") : id;
  }

  public static UUID convertStringToUUID(String id){
    return UUID.fromString(id);
  }
}
