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
public class ItemComponentId implements Serializable {

    @Column(name = "item_id")
    private UUID itemId;

    @Column(name = "component_id")
    private UUID componentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemComponentId that = (ItemComponentId) o;
        return itemId.equals(that.itemId) && componentId.equals(that.componentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, componentId);
    }
}
