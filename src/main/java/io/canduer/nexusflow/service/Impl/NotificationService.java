package io.canduer.nexusflow.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Async
    public void sendInvoiceEmail(String email) {
        log.info("Email Started...{}", email);
        try{
          Thread.sleep(5000);
        } catch (InterruptedException ex) {
           Thread.currentThread().interrupt();
        }
        log.info("Email Sent.{}", email);
    }
}
