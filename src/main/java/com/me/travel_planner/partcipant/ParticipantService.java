package com.me.travel_planner.partcipant;

import com.me.travel_planner.trip.Trip;
import com.me.travel_planner.trip.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    public List<Participant> registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();
        participantRepository.saveAll(participants);
        return participants;
    }

    public ParcitipantResponseDTO registerParticipantToEvent(String email, Trip trip) {
        Participant participant = new Participant(email, trip);
        participantRepository.save(participant);
        return new ParcitipantResponseDTO(participant.getId());
    }

    public List<ParticipantResponseGetDTO> getParticipantsByTrip(UUID tripId) {
        List<ParticipantResponseGetDTO> participants = participantRepository.findByTripId(tripId).stream().map(part -> new ParticipantResponseGetDTO(part.getId(), part.getName(), part.getEmail(), part.getIsConfirmed())).toList();

        return participants;
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {}

    public void triggerConfirmationEmailToParticipant(String email) {}
}
