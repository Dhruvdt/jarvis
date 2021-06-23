package com.unravel.scout.repository;

import com.unravel.scout.model.entity.Destinations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinationRepository extends MongoRepository<Destinations, String> {
  @Override
  Optional<Destinations> findById(String s);
}
