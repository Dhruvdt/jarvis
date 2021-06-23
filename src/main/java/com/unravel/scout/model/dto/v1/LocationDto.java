package com.unravel.scout.model.dto.v1;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class LocationDto {
    private BigDecimal latitude;
    private BigDecimal longitude;
}
