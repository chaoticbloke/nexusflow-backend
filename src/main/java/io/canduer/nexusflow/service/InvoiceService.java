package io.canduer.nexusflow.service;
import io.canduer.nexusflow.dto.ApiResponse;
import io.canduer.nexusflow.dto.InvoiceDTO;
import org.springframework.data.domain.Page;

public interface InvoiceService {


    // Create
    ApiResponse<InvoiceDTO> createInvoice(InvoiceDTO invoiceDTO, String customerId);

    // Read
    ApiResponse<InvoiceDTO> getInvoice(String invoiceNumber);

    ApiResponse<Page<InvoiceDTO>> getInvoices(int pageNumber, int pageSize);

    ApiResponse<Page<InvoiceDTO>> getCustomerInvoices(String customerId, int pageNumber, int pageSize);

    // Search
    ApiResponse<Page<InvoiceDTO>> searchInvoices(String invoiceNumber, int pageNumber, int pageSize);

    // Update
    ApiResponse<InvoiceDTO> updateInvoiceStatus(String invoiceNumber, InvoiceDTO invoiceDTO);

    // Delete
    void deleteInvoice(String invoiceNumber);

    byte[] downloadInvoicePdf(String invoiceNumber);
}
