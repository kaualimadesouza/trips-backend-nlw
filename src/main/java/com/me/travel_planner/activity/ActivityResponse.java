package com.me.travel_planner.activity;

import java.util.UUID;

public record ActivityResponse(UUID id, String title, String occurs_at) {
}
