package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "countries")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at"})
public class Country extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // @Id
    // @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    // private UUID id;

    @Column(name = "destination_name")
    private String name;

    @Column(name = "latitude", columnDefinition="Decimal(11,8)")
    private BigDecimal latitude;

    @Column(name = "longitude", columnDefinition="Decimal(11,8)")
    private BigDecimal longitude;

    @Column(name = "flag_url")
    private String flagUrl;

    @Column(name = "country_code")
    private Integer countryCode;

    // @OneToOne
    // private Image image;

}
