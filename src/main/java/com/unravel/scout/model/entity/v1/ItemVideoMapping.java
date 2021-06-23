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
@Table(name = "item_video_mapping")
@Entity
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at"})
public class ItemVideoMapping {

    @EmbeddedId
    private ItemVideoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(referencedColumnName = "item_id")   // TODO: verify
    @MapsId("itemId")
    private ItemDetail item;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private Video video;

    @Column(name = "is_primary")
    private boolean isPrimary;

    public ItemVideoMapping(ItemDetail item, Video video) {
        this.id = new ItemVideoId(item.getId(), video.getId());
        this.item = item;
        this.video = video;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVideoMapping that = (ItemVideoMapping) o;
        return item.equals(that.item) && video.equals(that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, video);
    }
}