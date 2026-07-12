package io.canduer.nexusflow.service.Impl.kafka;

import io.canduer.nexusflow.dto.events.InvoiceCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvoiceDltConsumer {

    @KafkaListener(topics = "invoice-created-dlt", groupId = "dlt-group")
    public void consume(InvoiceCreatedEvent event) {

        log.error("DLT RECEIVED FAILED EVENT {}", event);
    }
}