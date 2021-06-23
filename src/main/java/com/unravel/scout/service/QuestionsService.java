package com.unravel.scout.service;


import com.unravel.scout.model.entity.v1.Question;
import com.unravel.scout.repositories.QuestionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionsService {

    private final QuestionsRepository questionsRepository;
    @Autowired
    public QuestionsService(QuestionsRepository questionsRepository) {
        this.questionsRepository = questionsRepository;
    }

    public List<Question> getQuestionsDocument(Boolean isJointly) {
        return questionsRepository.findAllByIsJointly(isJointly);
     }
}
