package com.unravel.scout.repositories;

import com.unravel.scout.model.entity.v1.ItemDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemDetailRepository extends JpaRepository<ItemDetail, UUID> {

    Optional<ItemDetail> findById(UUID id);
    Optional<ItemDetail> findByIdAndUserId(UUID id, long userId);

    List<ItemDetail> findByItemType(String itemType);
    List<ItemDetail> findByItemType(String itemType, Pageable pageable);

    List<ItemDetail> findByItemTypeAndIsDefault(String itemType,Boolean bool);
    List<ItemDetail> findByItemTypeAndIsDefault(String itemType, Pageable pageable,Boolean bool);

    List<ItemDetail> findByItemTypeAndIsDefaultAndUserId(String itemType,Boolean bool,long userId);
    List<ItemDetail> findByItemTypeAndIsDefaultAndUserId(String itemType, Pageable pageable, Boolean bool,long userId);

    @Query(value = "select * from item_details where item_type =:type", nativeQuery = true)
    List<ItemDetail> fetchData(String type);

    @Query(value = "SELECT * FROM item_details WHERE item_sub_type_id =:id limit :limit", nativeQuery = true)
    public List<ItemDetail> findTopN(Long id, @Param("limit") int limit);

    public List<ItemDetail> findByItemTypeAndId(String itemType,UUID id,Pageable pageable);

    @Transactional
    @Override
    void deleteById(UUID uuid);
}
