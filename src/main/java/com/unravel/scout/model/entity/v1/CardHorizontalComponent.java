package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Table(name = "card_horizontal_components")
@Entity
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "component", "created_at", "updated_at"})
public class CardHorizontalComponent extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private Component component;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_item_id", insertable = false, updatable = false)
    private ItemDetail componentItem;

    @Column(name = "score")
    private Double score;

}