package com.unravel.scout.utils;

public final class EndpointRoutesV1 {
  private static final String API = "/api";
  private static final String VERSION = "/v1";
  public static final String ROOT_ENDPOINT = "/api/v1/trip/";
  public static final String USER_BASE_ENDPOINT = "/api/v1/trip/user";
  public static final String DESTINATIONS_BASE_ENDPOINT = "/api/v1/trip/destination";

  public static final String _ID = "/{id}";
  public static final String CREATE_TRIP = "/createtrip";
  public static final String QUESTIONS = "/questions";
  public static final String QUESTIONS_LIST = "/questions/list";
  public static final String CREATE_NEW_TRIP = "new/createtrip";
  public static final String DELETE_NEW_TRIP = "new/delete-trip/{tripId}";
  public static final String FETCH_TRIP = "/find";
  public static final String FETCH_RECOMMENDED_TRIP_BY_USERID = "/recommended/details";
  public static final String FETCH_TRIP_AND_MAPDATA = "/landing/data";
  public static final String FETCH_TRIP_COUNTRY = "/find/country";
  public static final String TRIP_ITINERARY = "itinerary";
  public static final String RENAME_TRIP = "update/rename";

  public static final String DESTINATIONS_TRIP = "destination/{destinationName}";
  public static final String DESTINATIONS = "destinations";
  public static final String DESTINATIONS_LIST = "destinations/list";
  public static final String SHARE = "share";

  // USERS ENDPOINT
  public static final String USERS_TRIP = "user/{userId}";
  public static final String USER_RECENTLY_VIEWED = "/recently/viewed/{userId}";
  public static final String USER_VIEW = "user/view";

  public static final String ADD_USERS_TRIP = "user/add";
  public static final String REMOVE_USERS_TRIP = "user/remove/{userId}/{itrId}";
  public static final String UPDATE_TRIP = "update";
  public static final String DELETE_TRIP = "delete/{tripId}";
  public static final String LIKE_TRIP_ITEM = "like";

  public static final String USER_END_POINT = "/api/v1/user/";
  // Item
  public static class ItemsURI {
    private ItemsURI() { }

    public static final String ROOT = API + VERSION + "/item";

    public static final String ItemDetails = "/details/{itemId}";
    public static final String ItemDetail = "/detail";
    public static final String Components = "/components";
    public static final String RelatedTrips = "/related/trips";
    public static final String RelatedUserTrips = "/related/trips/user";
  }
  // Trip Itinerary
  public static class Itinerary {
    private Itinerary() { }

    public static final String ROOT = ROOT_ENDPOINT;
    public static final String ADD_DAY = "update/addDay";
    public static final String REMOVE_DAY = "update/removeDay";
    public static final String ADD_ITEM = "update/addItem";
    public static final String REMOVE_ITEM = "update/removeItem";
    public static final String REPLACE_ITEM = "update/replaceItem";
    public static final String UPDATE_DATES = "update/dates";
    public static final String UPDATE_DAYS = "update/days";
    public static final String REORDER_ITEMS = "update/reOrder";
  }

  public static final String FETCH_TRIP_CATEGORY = "/find/category";
  public static final String FETCH_ITEM_BY_TRIP_SUBTYPE = "/find/items/subtype";
  public static final String FETCH_DESTINATION_CATEGORY = "/find/destination";
  public static final String FETCH_NEAR_BY_ITEMS = "/map/items/itinerary-days";
}