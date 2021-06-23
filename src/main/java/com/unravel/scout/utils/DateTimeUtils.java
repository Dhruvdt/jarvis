package com.unravel.scout.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeUtils {
  public static final String DATETIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
  public static final String DATE = "dd-MM-yyyy";

  public static String currentDatetimeString() {
    DateTime dateTime = new DateTime();
    DateTimeFormatter formatter = DateTimeFormat.forPattern(DATETIME_FORMAT);
    return formatter.print(dateTime);
  }

  public static String dateTime2String(DateTime timestamp) {
    DateTimeFormatter formatter = DateTimeFormat.forPattern(DATETIME_FORMAT);
    return formatter.print(timestamp);
  }

  public static LocalDate string2Date(String stringDate) {
    DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE);
    return formatter.parseLocalDate(stringDate);
  }

  public static DateTime string2DateTime(String stringDateTime) {
    DateTimeFormatter formatter = DateTimeFormat.forPattern(DATETIME_FORMAT);
    return formatter.parseDateTime(stringDateTime);
  }

  public static DateTime string2DateTime(String stringDateTime, String format) {
    DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
    return formatter.parseDateTime(stringDateTime);
  }

  public static java.time.LocalDateTime strToLocalDateTime(String dateStr) {
    java.time.format.DateTimeFormatter javaFormatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    return java.time.LocalDateTime.parse(dateStr, javaFormatter);
  }
}
