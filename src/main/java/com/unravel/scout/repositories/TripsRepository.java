package com.unravel.scout.repositories;

import com.unravel.scout.model.entity.v1.ItemDetail;
import com.unravel.scout.model.entity.v1.ItineraryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TripsRepository extends JpaRepository<ItineraryItem, UUID> {

    @Query("select trip.id from ItemDetail trip where trip.id = :uuid")
    Optional<ItemDetail> findTripById(@Param("uuid") UUID uuid);

    @Transactional
    @Modifying(flushAutomatically = true)
    @Query("update ItemDetail trip set trip.displayName = :name where trip.id = :uuid")
    int updateTripName(@Param("uuid") UUID uuid, @Param("name") String name);

    @Query(value="select * from itinerary_items where trip_id =:uuid", nativeQuery = true)
    List<ItineraryItem> findByTripId(UUID uuid);

    Optional<ItineraryItem> findByTrip_IdAndItem_Id(UUID tripId, UUID itemId);

    List<ItineraryItem> findByTripIdAndDay(UUID uuid, int day);

    @Query(value = "\r\n"
            + "SELECT bin_to_uuid(id.id) id,id.name,ist.item_type,id.latitude,id.longitude,id.item_sub_type_id,ist.sub_type,a.city ,a.country, ( 3959 * acos( cos( radians(:radiusLat) ) * cos( radians( latitude ) )\r\n"
            + "    * cos( radians( longitude ) - radians(:radiusLong) ) + sin( radians(:radiusLat) ) * sin(radians(latitude)) ) ) AS distance\r\n"
            + "FROM item_details id\r\n"
            + "inner join item_image_mapping iim on id.id = iim.item_id \r\n"
            + "inner join item_sub_type ist on id.item_sub_type_id = ist.id \r\n"
            + "inner join address a on a.id = id.id\r\n"
            + " HAVING distance < :locationRange ORDER BY distance", nativeQuery = true)
    List<Object[]> findByTripIdAndItineraryDay(double radiusLat, double radiusLong, double locationRange);

    @Query(value = "SELECT DISTINCT idt from ItineraryItem it INNER JOIN ItemDetail idt " +
        "ON it.trip.id = idt.id WHERE it.item.id IN :itemIds AND idt.isDefault = true")
    List<ItemDetail> findByItemIdInCuratedTrips(List<UUID> itemIds);

    @Query(value = "SELECT DISTINCT idt from ItemDetail idt WHERE idt.userId = :userId " +
        "AND idt.itemType = 'TRIP' AND idt.isDefault = false")
    List<ItemDetail> findUserTrips(Long userId);

    @Query(nativeQuery = true,
        value = "SELECT DISTINCT bin_to_uuid(it.id) as trip_id, bin_to_uuid(ii.item_id) as item_id, it.user_id " +
            "FROM item_details it INNER JOIN itinerary_items ii ON it.id = ii.trip_id " +
            "WHERE it.user_id = :userId AND ii.item_id = :itemId AND it.is_default = false")
    List<Object[]> findUserTripsByItemId(Long userId, UUID itemId);

}
