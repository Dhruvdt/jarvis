package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddItemToDayRequest {
    private String tripId;
    private String userId;
    private int day;
    private String itemId;
}
