package com.unravel.scout.repository;

import com.unravel.scout.model.entity.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends MongoRepository<Question, String> {

  Optional<Question> findBy_id(String _id);

  List<Question> findAllByIsJointly(boolean isJointly);
}
