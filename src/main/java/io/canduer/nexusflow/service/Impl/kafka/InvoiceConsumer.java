package io.canduer.nexusflow.service.Impl.kafka;

import io.canduer.nexusflow.dto.events.InvoiceCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvoiceConsumer {

    //@KafkaListener(topics = "invoice-created", groupId = "notification-group")
    public void consume(InvoiceCreatedEvent event) {
        log.info("InvoiceConsumer:invoice created event received = {}", event);
    }
}
