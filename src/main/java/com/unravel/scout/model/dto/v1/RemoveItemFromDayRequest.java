package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RemoveItemFromDayRequest {
    private String tripId;
    private String userId;
    private String itemId;
    private int day;
    // private int order;
}
