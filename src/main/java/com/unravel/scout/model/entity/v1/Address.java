package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Table(name = "address")
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernateLazyInitializer", "hibernate_lazy_initializer", "handler", "created_at", "updated_at", "item"})
public class Address extends BaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "continent")
    private String continent;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "website", columnDefinition="TEXT")
    private String website;

    @OneToOne(mappedBy = "address", fetch = FetchType.LAZY)
    private ItemDetail item;

}