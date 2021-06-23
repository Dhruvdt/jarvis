package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Table(name = "item_tags")
@Entity
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "item", "created_at", "updated_at"})
public class ItemTag extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "score")
    private Double score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id"/*, referencedColumnName = "item_id"*/)
    private ItemDetail item;

}