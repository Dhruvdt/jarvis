package com.unravel.scout.model.request.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.istack.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTripDto {
     String user_id;
     String trip_id;
     int duration_days;
     String start_date;
     String end_date;
     String destination;
     Boolean is_jointly;
     JsonNode questions;
     String trip_name;
}
