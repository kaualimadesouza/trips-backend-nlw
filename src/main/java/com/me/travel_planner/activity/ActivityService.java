package com.me.travel_planner.activity;

import com.me.travel_planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityResponse registerActivity(Trip trip, ActivityRequestDTO details) {
        Activity act = new Activity(details.title(), details.occurs_at(), trip);
        activityRepository.save(act);
        return new ActivityResponse(act.getId(), details.title(), details.occurs_at());
    }

    public List<ActivityResponse> getActivitiesByTrip(UUID tripId) {
        return activityRepository.findByTripId(tripId).stream().map(act -> new ActivityResponse(act.getId(), act.getTitle(), act.getOccursAt().format(DateTimeFormatter.ISO_DATE_TIME))).toList();
    }

}
