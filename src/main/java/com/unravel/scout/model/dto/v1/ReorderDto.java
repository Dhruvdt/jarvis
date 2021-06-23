package com.unravel.scout.model.dto.v1;

import lombok.Data;

@Data
public class ReorderDto {
    private String itemId;
    private int day;
    private int order;
}
