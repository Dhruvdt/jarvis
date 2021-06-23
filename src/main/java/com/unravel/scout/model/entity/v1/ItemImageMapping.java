package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Table(name = "item_image_mapping")
@Entity
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at"})
public class ItemImageMapping {

    @EmbeddedId
    private ItemImageId id;

    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(referencedColumnName = "item_id")   // TODO: verify
    @MapsId("itemId")
    private ItemDetail item;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("imageId")
    private Image image;

    @Column(name = "position")
    private Integer order;

    public ItemImageMapping(ItemDetail item, Image image) {
        this.id = new ItemImageId(item.getId(), image.getId());
        this.item = item;
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemImageMapping that = (ItemImageMapping) o;
        return item.equals(that.item) && image.equals(that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, image);
    }
}
