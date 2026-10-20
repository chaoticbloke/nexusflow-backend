package io.canduer.nexusflow.dto.events;

public record UserRegisteredEvent(String userId, String email, String firstName) {
}
