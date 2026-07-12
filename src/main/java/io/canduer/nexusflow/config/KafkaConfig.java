package io.canduer.nexusflow.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /*
    Initial attempt

    Wait 3 sec
    ------------

    Retry #1

    Wait 3 sec
    ------------
    Retry #2

    Give up. (1 original + 2 retries)
     */
    @Bean
    public DefaultErrorHandler errorHandler() {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(kafkaTemplate, 
                        (record, ex) -> new TopicPartition("invoice-created-dlt", record.partition())
                );

        FixedBackOff backOff = new FixedBackOff(3000L, 2);

        return new DefaultErrorHandler(recoverer, backOff);
    }
}