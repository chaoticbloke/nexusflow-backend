package io.canduer.nexusflow.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DashboardAsyncService {
    @Async
    public CompletableFuture<String> getCustomerStats() {
        sleep();
        return CompletableFuture.completedFuture("100 Customers");
    }

    @Async
    public CompletableFuture<String> getInvoiceStats() {
        sleep();
        return CompletableFuture.completedFuture("5090 Invoices");
    }

    private void sleep() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
