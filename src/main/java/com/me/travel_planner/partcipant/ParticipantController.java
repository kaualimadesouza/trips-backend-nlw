package com.me.travel_planner.partcipant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController 
@RequestMapping("/participant")
public class ParticipantController {

    @Autowired
    private ParticipantRepository participantRepository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequestDTO details) {
        Optional<Participant> participant = participantRepository.findById(id);

        if(participant.isPresent()) {
            Participant rawParticipant = participant.get();
            rawParticipant.setName(details.name());
            rawParticipant.setIsConfirmed(true);
            participantRepository.save(rawParticipant);
            return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();
    }
}
