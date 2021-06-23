package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "item_sub_type")
@Entity
@TypeDefs({@TypeDef(name = "json", typeClass = JsonType.class)})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at"})
public class ItemSubType extends BaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "item_type")
    private String itemType;

    @Column(name = "sub_type")
    private String subType;

    @Type(type = "json")
    @Column(name = "image_urls", columnDefinition = "json")
    private JsonNode imageUrls;
}