package com.unravel.scout.model.dto.v1;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.unravel.scout.model.entity.v1.ItemSubType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDetailDto {

    private Long id;
    private String itemType;
    private String subType;

    private JsonNode images;


    public static List<CategoryDetailDto> mapToCategoryListDto(List<ItemSubType> list) {
        List<CategoryDetailDto> response  = new ArrayList<CategoryDetailDto>();
        for(int i=0;i<list.size();i++)
        {
            var itemBuilder = CategoryDetailDto.builder();
            itemBuilder.id(list.get(i).getId())
                    .itemType(list.get(i).getItemType())
                    .subType(list.get(i).getSubType())
                    .images(list.get(i).getImageUrls());

            response.add(itemBuilder.build());

        }   return response;

    }


}
