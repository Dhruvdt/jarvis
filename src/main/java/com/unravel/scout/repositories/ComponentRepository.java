package com.unravel.scout.repositories;

import com.unravel.scout.model.entity.v1.Component;
import com.unravel.scout.model.entity.v1.ItemComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComponentRepository extends JpaRepository<Component, UUID> {

    @Query("SELECT DISTINCT ic FROM ItemComponent ic JOIN ic.component c WHERE ic.id.itemId = :itemId AND c.name = :name")
    List<ItemComponent> findComponentsByNameAndItemId(@Param("name")String componentName,
                                                      @Param("itemId")UUID itemId);
}
