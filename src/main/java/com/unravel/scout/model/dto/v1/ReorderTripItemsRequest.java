package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReorderTripItemsRequest {
    private String tripId;
    private String userId;
    private List<ReorderDto> itemsToReorder;
}
