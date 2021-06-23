package com.unravel.scout.model.entity.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "itinerary_items")
@Entity
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "trip", "created_at", "updated_at"})
public class ItineraryItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private ItemDetail trip;

    @Column(name = "day")
    private Integer day;

    @Column(name = "pace")
    private String pace;

    @Column(name = "position")
    private Integer order;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "duration_min")
    private Integer durationMin;

    @Column(name = "transit_duration_min")
    private Integer transitDurationMin;

    @OneToOne
    @JoinColumn(name = "item_id")
    private ItemDetail item;

    public ItineraryItem getCopy() {
        return ItineraryItem.builder()
            .trip(this.trip)
            .day(this.day)
            .pace(this.pace)
            .order(this.order)
            .startTime(this.startTime)
            .endTime(this.endTime)
            .durationMin(this.durationMin)
            .transitDurationMin(this.transitDurationMin)
            .item(this.item)
            .build();
    }
}