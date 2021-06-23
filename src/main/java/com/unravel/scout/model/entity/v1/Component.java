package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.unravel.scout.model.enums.ComponentType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Table(name = "components")
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at"})
public class Component extends BaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "component_type")
    @Enumerated(EnumType.STRING)
    private ComponentType componentType;

    @OneToMany(mappedBy = "component", fetch = FetchType.LAZY)
    private Set<HtmlComponent> htmlComponents;

    @OneToMany(mappedBy = "component", fetch = FetchType.LAZY)
    private Set<CardHorizontalComponent> cardHorizontalComponents;

}