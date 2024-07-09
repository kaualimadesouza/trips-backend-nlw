package com.me.travel_planner.activity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    public List<Activity> findByTripId(UUID tripId);
}
