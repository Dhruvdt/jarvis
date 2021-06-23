package com.unravel.scout.model.dto.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Duration;

public class PropertiesDto {

    public static JsonNode getPropertiesDto(JsonNode properties) {
        // price
        JsonNode priceNode = properties.get("price");
        if (priceNode != null && !priceNode.isNull() && priceNode.isArray()) {
            for (JsonNode item: priceNode) {
                ((ObjectNode) item).put("text", "From");
            }
        }
        // duration
        JsonNode durationMins = properties.get("duration_min");
        if (durationMins != null && !durationMins.isNull()) {
            String minutesStr = durationMins.asText();
            Duration duration = Duration.ofMinutes(Long.parseLong(minutesStr));
            ((ObjectNode) properties).put("duration",
                duration.toHours() + " hrs " + duration.toMinutesPart() + " mins");
            ((ObjectNode) properties).remove("duration_min");
        }

        return properties;
    }
}
