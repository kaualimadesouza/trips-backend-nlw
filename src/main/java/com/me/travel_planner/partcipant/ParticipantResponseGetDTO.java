package com.me.travel_planner.partcipant;

import java.util.UUID;

public record ParticipantResponseGetDTO(UUID id, String name, String email, Boolean isConfirmed) {
}
