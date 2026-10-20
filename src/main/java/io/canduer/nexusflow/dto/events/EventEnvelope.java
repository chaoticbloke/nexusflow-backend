package io.canduer.nexusflow.dto.events;

import java.util.UUID;

public record EventEnvelope<T>(UUID eventId, String eventType, int version, T payload) {}