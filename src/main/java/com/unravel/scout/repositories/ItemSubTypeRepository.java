package com.unravel.scout.repositories;

import com.unravel.scout.model.entity.v1.ItemSubType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSubTypeRepository extends JpaRepository<ItemSubType, Long> {
}
