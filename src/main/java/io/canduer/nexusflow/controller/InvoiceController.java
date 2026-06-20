package io.canduer.nexusflow.controller;

import io.canduer.nexusflow.dto.ApiResponse;
import io.canduer.nexusflow.dto.InvoiceDTO;
import io.canduer.nexusflow.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("")
    ApiResponse<Page<InvoiceDTO>> getInvoices(@RequestParam(value = "page", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return invoiceService.getInvoices(pageNumber, pageSize);
    }

    @PostMapping("/create/{customerId}")
    ApiResponse<InvoiceDTO> e(@RequestBody @Valid InvoiceDTO invoice, @PathVariable String customerId) {
        return invoiceService.createInvoice(invoice, customerId);
    }

    @GetMapping("/search/{invoiceNumber}")
    ApiResponse<Page<InvoiceDTO>> searchInvoice(@PathVariable String invoiceNumber, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return invoiceService.searchInvoices(invoiceNumber, page, size);
    }


    @PostMapping("/update/{invoiceNumber}/{status}")
    ApiResponse<InvoiceDTO> updateInvoice(@PathVariable String invoiceNumber, @PathVariable String status) {
        return invoiceService.updateInvoiceStatus(invoiceNumber, status);
    }

    @DeleteMapping("/delete/{invoiceNumber}")
    void deleteInvoice(@PathVariable String invoiceNumber) {
         invoiceService.deleteInvoice(invoiceNumber);
    }
}
