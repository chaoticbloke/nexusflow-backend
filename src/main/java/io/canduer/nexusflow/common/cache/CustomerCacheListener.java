package io.canduer.nexusflow.common.cache;

import io.canduer.nexusflow.dto.events.CustomerUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CustomerCacheListener {

    private final RedisCacheService redisCacheService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerUpdated(CustomerUpdatedEvent customerUpdatedEvent) {

        redisCacheService.delete(RedisCacheKeys.customer(customerUpdatedEvent.getCustomerId()));
    }
}