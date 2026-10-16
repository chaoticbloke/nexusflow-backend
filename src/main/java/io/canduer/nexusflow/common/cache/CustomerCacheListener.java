package io.canduer.nexusflow.common.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CustomerCacheListener {

    private final RedisCacheService redisCacheService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerUpdated(String customerId) {

        redisCacheService.delete(RedisCacheKeys.customer(customerId));
    }
}