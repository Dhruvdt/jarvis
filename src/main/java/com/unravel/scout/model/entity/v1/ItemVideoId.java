package com.unravel.scout.model.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ItemVideoId implements Serializable {

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "video_id")
    private UUID videoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemVideoId that = (ItemVideoId) o;
        return itemId.equals(that.itemId) && videoId.equals(that.videoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, videoId);
    }
}
