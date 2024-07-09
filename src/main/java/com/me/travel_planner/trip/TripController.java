package com.me.travel_planner.trip;

import com.me.travel_planner.activity.ActivityRequestDTO;
import com.me.travel_planner.activity.ActivityResponse;
import com.me.travel_planner.activity.ActivityService;
import com.me.travel_planner.link.LinkResponseDTO;
import com.me.travel_planner.link.LinkResquestDTO;
import com.me.travel_planner.link.LinkService;
import com.me.travel_planner.partcipant.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    public TripRepository tripRepository;

    @Autowired
    private ParticipantService partcipantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;


    // TRIP

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public ResponseEntity<TripResponseDTO> createTrip(@RequestBody TripRequestDTO tripRequestDTO) {
        Trip newTrip = new Trip(tripRequestDTO);
        tripRepository.save(newTrip);
        TripResponseDTO tripResponseDTO = new TripResponseDTO(
                newTrip.getId(),
                newTrip.getDestination(),
                newTrip.getStartsAt().format(DateTimeFormatter.ISO_DATE_TIME),
                newTrip.getEndsAt().format(DateTimeFormatter.ISO_DATE_TIME),
                newTrip.getIsConfirmed(),
                newTrip.getOwnerName(),
                newTrip.getOwnerEmail()
        );
        List<Participant> part = partcipantService.registerParticipantsToEvent(tripRequestDTO.emails_to_invite(), newTrip);
        return ResponseEntity.ok(tripResponseDTO);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public ResponseEntity<List<TripResponseDTO>> getAllTrips() {
        List<TripResponseDTO> getTrips = tripRepository.findAll().stream().map(trips -> new TripResponseDTO(trips)).toList();
        return ResponseEntity.ok(getTrips);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDTO> getTripDetails(@PathVariable UUID id) {
        Optional<TripResponseDTO> tripDetails = tripRepository.findById(id).map(trip -> new TripResponseDTO(trip));
        return tripDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripResponseDTO details) {
        Optional<Trip> tripDetails = tripRepository.findById(id);

        if(tripDetails.isPresent()) {
            Trip trip = tripDetails.get();

            trip.setDestination(details.destination());
            trip.setStartsAt(LocalDateTime.parse(details.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            trip.setEndsAt(LocalDateTime.parse(details.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
            tripRepository.save(trip);
            return ResponseEntity.ok(trip);
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> tripDetails = tripRepository.findById(id);

        if(tripDetails.isPresent()) {
            Trip trip = tripDetails.get();
            trip.setIsConfirmed(true);
            tripRepository.save(trip);
            partcipantService.triggerConfirmationEmailToParticipants(id);
            return ResponseEntity.ok(trip);
        }
        return ResponseEntity.notFound().build();
    }


    // PARTICIPANT

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/{id}/invite")
    public ResponseEntity<ParcitipantResponseDTO> invite(@PathVariable UUID id, @RequestBody ParticipantRequestDTO details) {
        Optional<Trip> trip = tripRepository.findById(id);

        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            ParcitipantResponseDTO parcitipantResponse = partcipantService.registerParticipantToEvent(details.email(), rawTrip);

            if(rawTrip.getIsConfirmed()) {
                partcipantService.triggerConfirmationEmailToParticipant(details.email());
            }

            return ResponseEntity.ok(parcitipantResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantResponseGetDTO>> getParticipantsById(@PathVariable UUID id) {
        Optional<Trip> trip = tripRepository.findById(id);
        if(trip.isPresent()) {
            List<ParticipantResponseGetDTO> participants = partcipantService.getParticipantsByTrip(id);

            return ResponseEntity.ok(participants);
        }
        return ResponseEntity.notFound().build();
    }

    // ACTIVIES

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/{tripId}/activities")
    public ResponseEntity<ActivityResponse> createActivity(@PathVariable UUID tripId, @RequestBody ActivityRequestDTO details) {
        Optional<Trip> trip = tripRepository.findById(tripId);
        if(trip.isPresent()) {
            ActivityResponse act = activityService.registerActivity(trip.get(), details);
            return ResponseEntity.ok(act);
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{tripId}/activities")
    public ResponseEntity<List<ActivityResponse>> getActivitiesByTripId(@PathVariable UUID tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);
        if(trip.isPresent()) {
            List<ActivityResponse> acts = activityService.getActivitiesByTrip(tripId);

            return ResponseEntity.ok(acts);
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/{tripId}/links")
    public ResponseEntity<LinkResponseDTO> createLink(@PathVariable UUID tripId, @RequestBody LinkResquestDTO details) {
        Optional<Trip> trip = tripRepository.findById(tripId);
        if(trip.isPresent()) {
            LinkResponseDTO link = linkService.registerLink(trip.get(), details);
            return ResponseEntity.ok(link);
        }
        return ResponseEntity.notFound().build();
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/{tripId}/links")
    public ResponseEntity<List<LinkResponseDTO>> getlinksByTripId(@PathVariable UUID tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);
        if(trip.isPresent()) {
            List<LinkResponseDTO> links = linkService.getlinksByTrip(tripId);

            return ResponseEntity.ok(links);
        }
        return ResponseEntity.notFound().build();
    }
}