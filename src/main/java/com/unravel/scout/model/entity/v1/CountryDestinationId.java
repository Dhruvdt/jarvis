package com.unravel.scout.model.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CountryDestinationId implements Serializable {
    @Column(name = "country_id")
    private UUID countryId;

    @Column(name = "destination_id")
    private UUID destinationId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryDestinationId that = (CountryDestinationId) o;
        return countryId.equals(that.countryId) && destinationId.equals(that.destinationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryId, destinationId);
    }
}
