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
@Table(name = "item_components")
@Entity
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "created_at", "updated_at"})
public class ItemComponent {

    @EmbeddedId
    private ItemComponentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(referencedColumnName = "item_id")   // TODO: verify
    @MapsId("itemId")
    private ItemDetail item;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("componentId")
    private Component component;

    @Column(name = "position")
    private Integer order;

    public ItemComponent(ItemDetail item, Component component) {
        this.id = new ItemComponentId(item.getId(), component.getId());
        this.item = item;
        this.component = component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemComponent that = (ItemComponent) o;
        return item.equals(that.item) && component.equals(that.component);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, component);
    }
}