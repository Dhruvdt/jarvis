package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "images")
@TypeDef(name = "json", typeClass = JsonType.class)
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at"})
public class Image extends BaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "ext_image_id")
    private String extImageId;

    @Column(name = "caption")
    private String caption;

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @Column(name = "copyright_text", columnDefinition="TEXT")
    private String copyrightText;

    @Column(name = "copyright_link", columnDefinition="TEXT")
    private String copyrightLink;

    @Column(name = "license")
    private String license;

    @Type(type = "json")
    @Column(name = "image_urls", columnDefinition = "json")
    private JsonNode imageUrls;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "image",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ItemImageMapping> items;
}
