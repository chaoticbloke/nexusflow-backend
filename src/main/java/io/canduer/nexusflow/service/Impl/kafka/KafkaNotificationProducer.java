package io.canduer.nexusflow.service.Impl.kafka;

import io.canduer.nexusflow.dto.events.InvoiceCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaNotificationProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishInvoiceCreatedV2(InvoiceCreatedEvent event) {
        //topic, key, event
        kafkaTemplate.send("invoice-created-v2", event.getCustomerId(), event);
    }
}
