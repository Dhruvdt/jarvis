package com.unravel.scout.model.dto.v1;

import lombok.Data;

import java.util.List;

@Data
public class GetRelatedTripsRequest {
    private List<String> itemIds;
    private String userId;
}
