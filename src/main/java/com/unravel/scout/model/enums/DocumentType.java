package com.unravel.scout.model.enums;

import java.util.EnumSet;

public enum DocumentType {
  USER("usr"),
  USER_VIEW("usrv"),
  USER_INTERACTION("usrint"),
  CHANGE_LOG("chl-"),
  TRIP_ITEM("trip-itm-"),
  TRIP("trip");

  public static EnumSet<DocumentType> userFamily = EnumSet.of(USER, USER_VIEW, USER_INTERACTION);
  public static EnumSet<DocumentType> productFamily = EnumSet.of(TRIP, TRIP_ITEM);

  public String idPrefix;

  DocumentType(String label) {
    this.idPrefix = label;
  }

  public static DocumentType getValueForPrefix(String label) {
    for (DocumentType t : values()) {
      if (t.idPrefix.equals(label)) {
        return t;
      }
    }
    return null;
  }

  public static Boolean isSameFamily(DocumentType documentType1, DocumentType documentType2) {
    return isUserFamily(documentType1) && isUserFamily(documentType2)
        || isProductFamily(documentType1) && isProductFamily(documentType2);
  }

  public static Boolean isSameFamily(String label1, String label2) {
    return isUserFamily(label1) && isUserFamily(label2)
        || isProductFamily(label1) && isProductFamily(label2);
  }

  public static Boolean isUserFamily(String label) {
    return DocumentType.userFamily.contains(getValueForPrefix(label));
  }

  public static Boolean isUserFamily(DocumentType targetType) {
    return DocumentType.userFamily.contains(targetType);
  }

  public static Boolean isProductFamily(String label) {
    return DocumentType.productFamily.contains(getValueForPrefix(label));
  }

  public static Boolean isProductFamily(DocumentType documentType) {
    return DocumentType.productFamily.contains(documentType);
  }

  public String getIdPrefix() {
    return this.idPrefix;
  }

  @Override
  public String toString() {
    return this.idPrefix;
  }
}
