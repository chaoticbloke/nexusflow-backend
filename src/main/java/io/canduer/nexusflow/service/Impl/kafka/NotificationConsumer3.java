package io.canduer.nexusflow.service.Impl.kafka;

import io.canduer.nexusflow.dto.events.InvoiceCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationConsumer3 {
    @KafkaListener(
            topics = "invoice-created-v2",
            groupId = "notification-group"
    )
    public void consume(InvoiceCreatedEvent event, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {

        log.info(
                "NotificationConsumer3 | Partition={} | Invoice={}",
                partition,
                event.getInvoiceNumber()
        );
    }
}
