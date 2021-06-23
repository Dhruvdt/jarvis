package com.unravel.scout.repositories;

import com.unravel.scout.model.entity.v1.CountryItemDestinationMapping;
import com.unravel.scout.model.entity.v1.ItemDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CountryMapping extends JpaRepository<CountryItemDestinationMapping, UUID> {


    Optional<CountryItemDestinationMapping> findByDestinationId(UUID id);
}
