package io.canduer.nexusflow.service.Impl.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.canduer.nexusflow.dto.events.EventEnvelope;
import io.canduer.nexusflow.dto.events.UserRegisteredEvent;
import io.canduer.nexusflow.entity.OutboxEvent;
import io.canduer.nexusflow.entity.User;
import io.canduer.nexusflow.enums.OutboxStatus;
import io.canduer.nexusflow.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void saveUserRegisteredEvent(User user) {

        UserRegisteredEvent event = new UserRegisteredEvent(
                user.getUserId().toString(),
                user.getEmail(),
                user.getFirstName()
        );

        UUID eventId = UUID.randomUUID();

        EventEnvelope<UserRegisteredEvent> envelope = new EventEnvelope<>(eventId, "USER_REGISTERED", 1, event);

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setId(eventId);
        outboxEvent.setAggregateType("USER");
        outboxEvent.setAggregateId(user.getUserId().toString());
        outboxEvent.setEventType("USER_REGISTERED");
        outboxEvent.setPayload(objectMapper.valueToTree(envelope));
        outboxEvent.setStatus(OutboxStatus.PENDING);

        outboxEventRepository.save(outboxEvent);
    }
}
