package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RenameTripRequest {
    private String tripId;
    private String userId;
    private String displayName;
}
