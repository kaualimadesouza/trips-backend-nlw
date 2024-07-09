package com.me.travel_planner.trip;

import com.me.travel_planner.partcipant.PartcipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    public TripRepository tripRepository;

    @Autowired
    private PartcipantService partcipantService;

    @PostMapping
    public ResponseEntity<String> createTrip(@RequestBody TripRequestDTO tripRequestDTO) {
        Trip newTrip = new Trip(tripRequestDTO);
        tripRepository.save(newTrip);
        partcipantService.registerParticipantsToEvent(tripRequestDTO.emails_to_invite(), newTrip.getId());
        return ResponseEntity.ok("Sucesso!");
    }

    @GetMapping
    public ResponseEntity<List<TripResponseDTO>> getAllTrips() {
        List<TripResponseDTO> getTrips = tripRepository.findAll().stream().map(trips -> new TripResponseDTO(trips)).toList();
        return ResponseEntity.ok(getTrips);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDTO> getTripDetails(@PathVariable UUID id) {
        Optional<TripResponseDTO> tripDetails = tripRepository.findById(id).map(trip -> new TripResponseDTO(trip));
        return tripDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
