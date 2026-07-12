package io.canduer.nexusflow.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdentifierUUIDGenerator {

    public String generateCustomerId() {
        return "CUST-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public String generateInvoiceNumber() {
        return "INV-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0,8)
                        .toUpperCase();
    }

    public String generateUserId() {
        return UUID.randomUUID().toString().substring(0,8);
    }
}
