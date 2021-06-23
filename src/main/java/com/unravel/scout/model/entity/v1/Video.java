package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Table(name = "videos")
@Entity
@TypeDefs({@TypeDef(name = "json", typeClass = JsonType.class)})
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at"})
public class Video extends BaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "caption")
    private String caption;

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @Type(type = "json")
    @Column(name = "image", columnDefinition = "json")
    private JsonNode image;

    @OneToMany(mappedBy = "video", fetch = FetchType.LAZY)
    private List<VideoTag> tags;

    @Type(type = "json")
    @Column(name = "video_urls", columnDefinition = "json")
    private JsonNode videoUrls;

    @Column(name = "source")
    private String source;

    @Column(name = "source_type")
    private String sourceType;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "video",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ItemVideoMapping> items;
}