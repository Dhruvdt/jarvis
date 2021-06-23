package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.unravel.scout.model.entity.v1.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressDto {
    private String city;
    private String country;
    private String countryCode;
    private String continent;

    public static AddressDto mapToAddressDto(Address address) {
        return AddressDto.builder()
            .city(address.getCity())
            .country(address.getCountry())
            .countryCode(address.getCountryCode())
            .continent(address.getContinent())
            .build();
    }
}
