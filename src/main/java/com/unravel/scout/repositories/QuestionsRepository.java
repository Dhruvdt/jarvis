package com.unravel.scout.repositories;

import com.unravel.scout.model.entity.v1.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsRepository extends JpaRepository<Question,Integer> {
    List<Question> findAllByIsJointly(Boolean mFlag);
}
