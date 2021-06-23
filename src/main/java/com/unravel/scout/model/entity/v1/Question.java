package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unravel.scout.model.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "questions")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "question_text")
    private String questionText;

    @Column(name = "question_name")
    private String questionName;

    @Column(name = "is_jointly",columnDefinition = "BOOLEAN")
    private Boolean isJointly;

    @Column(name = "question_order")
    private Integer question_order;

    @Column(name = "question_type",columnDefinition = "ENUM('TEXT', 'DATE', 'DESTINATION')")
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    public Set<QuestionOption> options;

}
