package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "questions_option")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class QuestionOption  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "options_value")
    private String optionsValue;

    @Column(name = "icon_url")
    private String iconUrl;

}
