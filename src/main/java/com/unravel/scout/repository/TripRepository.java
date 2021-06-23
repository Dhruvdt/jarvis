package com.unravel.scout.repository;

import com.unravel.scout.model.entity.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TripRepository extends MongoRepository<Trip, String> {

  Optional<Trip> findBy_id(String _id);

  void deleteBy_id(String s);
}
