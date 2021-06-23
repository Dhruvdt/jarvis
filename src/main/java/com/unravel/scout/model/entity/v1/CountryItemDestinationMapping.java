package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "country_item_destination_mapping")
@Entity
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at"})
public class CountryItemDestinationMapping {

    @EmbeddedId
    private CountryDestinationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("countryId")
    private ItemDetail country;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("destinationId")
    private ItemDetail destination;

    @OneToOne
    private ItemDetail defaultTrip;

    public CountryItemDestinationMapping(ItemDetail country, ItemDetail destination) {
        this.id = new CountryDestinationId(country.getId(), destination.getId());
        this.country = country;
        this.destination = destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryItemDestinationMapping that = (CountryItemDestinationMapping) o;
        return country.equals(that.country) && destination.equals(that.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, destination);
    }
}
