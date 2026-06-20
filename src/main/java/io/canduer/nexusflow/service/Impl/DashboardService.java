package io.canduer.nexusflow.service.Impl;

import io.canduer.nexusflow.dto.DashboardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final DashboardAsyncService dashboardAsyncService;

    @Cacheable("dashboard")
    public DashboardResponse buildDashboard() throws Exception {
        log.info("Building dashboard from DB");

        long start = System.currentTimeMillis();

        CompletableFuture<String> customerFuture = dashboardAsyncService.getCustomerStats();
        CompletableFuture<String> invoiceFuture = dashboardAsyncService.getInvoiceStats();

        CompletableFuture.allOf(customerFuture, invoiceFuture).join();

        long end = System.currentTimeMillis();

        log.info("Execution Time : {} ms", end - start);

        return DashboardResponse.builder()
                .customerStats(customerFuture.get())
                .invoiceStats(invoiceFuture.get())
                .build();
    }
}
