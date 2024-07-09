package com.me.travel_planner.trip;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public record TripResponseDTO(UUID id, String destination, String starts_at, String ends_at, Boolean is_confirmed,String owner_name, String owner_email) {
    public TripResponseDTO(Trip trip) {
        this(trip.getId(), trip.getDestination(), trip.getStartsAt().format(DateTimeFormatter.ISO_DATE_TIME), trip.getEndsAt().format(DateTimeFormatter.ISO_DATE_TIME), trip.getIsConfirmed(),trip.getOwnerName(), trip.getOwnerEmail());
    }
}
