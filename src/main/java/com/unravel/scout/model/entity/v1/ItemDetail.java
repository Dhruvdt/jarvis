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
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Table(name = "item_details")
@Entity
@TypeDefs({@TypeDef(name = "json", typeClass = JsonType.class)})
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at", "tripDays"})
public class ItemDetail extends BaseEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "item_type")
    private String itemType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_sub_type_id")
    private ItemSubType itemSubType;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", referencedColumnName = "id", columnDefinition = "BINARY(16)")
    private Address address;

    @Column(name = "description", columnDefinition="TEXT")
    private String description;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "item",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ItemImageMapping> images;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "item",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ItemVideoMapping> videos;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<ItemTag> tags;

    @Column(name = "share_url")
    private String shareUrl;

    @Column(name = "vendor_id")
    private String vendorId;

    @Type(type = "json")
    @Column(name = "properties", columnDefinition = "json")
    private JsonNode properties;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "item",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ItemComponent> components;

    @Column(name = "latitude", columnDefinition="Decimal(11,8)")
    private BigDecimal latitude;

    @Column(name = "longitude", columnDefinition="Decimal(11,8)")
    private BigDecimal longitude;

    @Column(name = "is_default", columnDefinition = "boolean default false")
    private Boolean isDefault;

    @OneToMany(
        mappedBy = "trip",
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<ItineraryItem> tripDays;

    @ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name = "item_destination_mapping",
            joinColumns = {@JoinColumn(name = "destination_id")},
            inverseJoinColumns = {@JoinColumn(name = "item_id")})
    private Set<ItemDetail> destinationItems;

    @ManyToMany(mappedBy = "destinationItems")
    private Set<ItemDetail> itemDestinations;

    @OneToMany(
            mappedBy = "country",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CountryItemDestinationMapping> countryDestinations;

    @Column(name = "user_id")
    private Long userId;

    // customer setters
    public void addExistingImage(ItemImageMapping existingMapping) {
        if (this.images == null) {
            this.images = new HashSet<>();
        }

        ItemImageMapping itemImageMapping = new ItemImageMapping(this, existingMapping.getImage());
        itemImageMapping.setOrder(existingMapping.getOrder());
        this.images.add(itemImageMapping);
        // existingMapping.getImage().getItems().add(itemImageMapping);
    }

    public void addExistingComponent(ItemComponent existingComponent) {
        if (this.components == null) {
            this.components = new HashSet<>();
        }

        ItemComponent itemComponent = new ItemComponent(this, existingComponent.getComponent());
        itemComponent.setOrder(existingComponent.getOrder());
        this.components.add(itemComponent);
    }

    public void addExistingVideo(ItemVideoMapping existingMapping) {
        if (this.videos == null) {
            this.videos = new HashSet<>();
        }

        ItemVideoMapping itemVideoMapping = new ItemVideoMapping(this, existingMapping.getVideo());
        this.videos.add(itemVideoMapping);
        // existingMapping.getVideo().getItems().add(itemVideoMapping);
    }

}