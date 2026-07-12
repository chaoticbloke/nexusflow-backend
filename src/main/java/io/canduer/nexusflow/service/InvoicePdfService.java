package io.canduer.nexusflow.service;


import io.canduer.nexusflow.entity.Invoice;

public interface InvoicePdfService {
    byte[] generateInvoicePdf(Invoice invoice);
}
