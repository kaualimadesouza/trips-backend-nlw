package com.me.travel_planner.activity;

import com.me.travel_planner.trip.Trip;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "activities")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(name = "occurs_at", nullable = false)
    private LocalDateTime occursAt;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    public Activity(String title, String s, Trip trip) {
        this.title = title;
        this.occursAt = LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME);
        this.trip = trip;
    }
}
