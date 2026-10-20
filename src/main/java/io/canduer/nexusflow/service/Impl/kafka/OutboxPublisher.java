package io.canduer.nexusflow.service.Impl.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import io.canduer.nexusflow.entity.OutboxEvent;
import io.canduer.nexusflow.enums.OutboxStatus;
import io.canduer.nexusflow.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, JsonNode> kafkaTemplate;

    private static final String USER_REGISTERED_TOPIC = "user-registered";
    private static final int BATCH_SIZE = 50;

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxEventRepository
                .findByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING, PageRequest.of(0, BATCH_SIZE));

        for (OutboxEvent event : events) {
            publishEvent(event);
        }
    }

    private void publishEvent(OutboxEvent event) {
        try {

            // Wait until Kafka acknowledges the message and throw TimeoutException. goes into catch block
            kafkaTemplate.send(USER_REGISTERED_TOPIC, event.getAggregateId(), event.getPayload())
                    .get(10, TimeUnit.SECONDS);

            event.setStatus(OutboxStatus.PUBLISHED);
            event.setPublishedAt(Instant.now());
            event.setLastError(null);

            outboxEventRepository.save(event);

            log.info("Successfully Published outbox event id={} type={}", event.getId(), event.getEventType());

        } catch (Exception exception) {
            event.setRetryCount(event.getRetryCount() + 1);
            event.setLastError(exception.getMessage());

            outboxEventRepository.save(event);

            log.error(
                    "Failed to publish outbox event id={}; it will be retried",
                    event.getId(),
                    exception
            );
        }
    }
}