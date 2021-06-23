package com.unravel.scout.model.dto.v1;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.unravel.scout.model.entity.v1.ItineraryItem;

import com.unravel.scout.model.enums.EnumItemSubType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItineraryLocationDto {

    private double latitude;
    private double longitude;
    private UUID item_id;
    private String name;
    private String displayName;
    private String item_type;
    private Long item_sub_type_id;
    private String item_sub_type_name;

    private AddressDto address;
    private LocationDto location;
    private Long item_type_id;

    public static ItineraryLocationDto mapToItineraryItemToLocationDto(ItineraryItem itineraryItem) {
        return ItineraryLocationDto.builder()
                .latitude(itineraryItem.getItem().getLatitude().doubleValue())
                .longitude(itineraryItem.getItem().getLongitude().doubleValue())
                .item_id(itineraryItem.getItem().getId())
                .name(itineraryItem.getItem().getName())
                .displayName(itineraryItem.getItem().getDisplayName())
                .item_type(itineraryItem.getItem().getItemSubType().getItemType())
                .item_sub_type_id(itineraryItem.getItem().getItemSubType().getId())
                .item_sub_type_name(itineraryItem.getItem().getItemSubType().getSubType())
                .item_type_id(checkITemTypeAndMapItemTypeId(itineraryItem.getItem().getItemSubType().getItemType()))
                .address(AddressDto.mapToAddressDto(itineraryItem.getItem().getAddress()))
                .location(LocationDto.builder()
                        .latitude(itineraryItem.getItem().getLatitude())
                        .longitude(itineraryItem.getItem().getLongitude()).build())
                .build();
    }


    public static List<HashMap<String, Object>> mapToCustomResponseToLocationDto(List<Object[]> respo) {
        Map<String, String> address = new HashMap();
        Map<String, Double> els_location = new HashMap();
        List<HashMap<String, Object>> itemsDetails = null;
        itemsDetails = respo.stream().map(q -> {
            HashMap<String, Object> res = new HashMap<>();
            res.put("item_id", q[0]);
            res.put("name", q[1]);
            res.put("item_type", q[2]);
            els_location.put("latitude", ((BigDecimal) q[3]).doubleValue());
            els_location.put("longitude", ((BigDecimal) q[4]).doubleValue());
            res.put("location", els_location);
            res.put("item_sub_type_id", q[5]);
            res.put("item_sub_type_name", q[6]);
            // res.put("image_urls",q[6]);
            address.put("city", (String) q[7]);
            address.put("country", (String) q[8]);
            res.put("address", address);
            res.put("item_type_id", checkITemTypeAndMapItemTypeId((String) q[2]));

            return res;
        }).collect(Collectors.toList());
        return itemsDetails;
    }


    public static List<Double> calculateMidPointLocations(double lat1, double lon1, double lat2, double lon2) {
        double dLon = Math.toRadians(lon2 - lon1);
        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        List<Double> latlong = new ArrayList<>();
        latlong.add(Math.toDegrees(lat3));
        latlong.add(Math.toDegrees(lon3));
        return latlong;
    }

    private static Long checkITemTypeAndMapItemTypeId(String subTypeName) {
        if (subTypeName.equals(EnumItemSubType.valueOf(subTypeName.toString()).displayStatus()))
            return 1L;
        else if (subTypeName.equals(EnumItemSubType.valueOf(subTypeName.toString()).displayStatus()))
            return 2L;
        else
            return 3L;
    }

    public static Map<String, Double> calculateCenterWithMutipleLocation(List<ItineraryLocationDto> locationsDtoList) {
        double pi = Math.PI / 180;
        double xpi = 180 / Math.PI;
        Map<String,Double> centerLatLong = new HashMap<>();


        if (locationsDtoList.size() == 1) {
            return centerLatLong;
        }

        double x = 0, y = 0, z = 0;
        for (ItineraryLocationDto c : locationsDtoList) {
            double latitude = c.getLatitude() * pi, longitude = c.getLongitude() * pi;
            double cl = Math.cos(latitude);//save it as we need it twice
            x += cl * Math.cos(longitude);
            y += cl * Math.sin(longitude);
            z += Math.sin(latitude);
        }
        int total = locationsDtoList.size();;
        x = x / total;
        y = y / total;
        z = z / total;

        double centralLongitude = Math.atan2(y, x);
        double centralSquareRoot = Math.sqrt(x * x + y * y);
        double centralLatitude = Math.atan2(z, centralSquareRoot);
        double lat = centralLatitude*xpi;
        double lng = centralLongitude*xpi;

        centerLatLong.put("latitude", lat);
        centerLatLong.put("longitude", lng);
        return centerLatLong;
    }

}