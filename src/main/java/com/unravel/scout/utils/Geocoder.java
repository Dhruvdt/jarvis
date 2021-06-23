package com.unravel.scout.utils;

import com.unravel.scout.model.entity.Geocode;

public class Geocoder {

  /**
   * https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
   * Calculate distance between two points in latitude and longitude taking into account height
   * difference. If you are not interested in height difference pass 0.0. Uses Haversine method as
   * its base.
   *
   * <p>geocode1 Start point geocode2 End point altitude1 Start altitude in meters altitude2 End
   * altitude in meters
   *
   * @returns Distance in Meters
   */
  public static double distance(
      Geocode geocode1, Geocode geocode2, double altitude1, double altitude2) {

    final int R = 6371; // Radius of the earth

    double latDistance = Math.toRadians(geocode2.getLatitude() - geocode1.getLatitude());
    double lonDistance = Math.toRadians(geocode2.getLongitude() - geocode1.getLongitude());
    double a =
        Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(geocode1.getLatitude()))
                * Math.cos(Math.toRadians(geocode2.getLatitude()))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    double distance = R * c * 1000; // convert to meters

    double height = altitude2 - altitude1;

    distance = Math.pow(distance, 2) + Math.pow(height, 2);

    return Math.sqrt(distance);
  }
}
