package io.canduer.nexusflow.controller;

import io.canduer.nexusflow.dto.ApiResponse;
import io.canduer.nexusflow.dto.InvoiceDTO;
import io.canduer.nexusflow.service.InvoicePdfService;
import io.canduer.nexusflow.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final InvoicePdfService pdfService;

    @GetMapping("")
    ApiResponse<Page<InvoiceDTO>> getInvoices(@RequestParam(value = "page", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return invoiceService.getInvoices(pageNumber, pageSize);
    }

    @PostMapping("/create/{customerId}")
    ApiResponse<InvoiceDTO> createInvoice(@RequestBody @Valid InvoiceDTO invoice, @PathVariable String customerId) {
        return invoiceService.createInvoice(invoice, customerId);
    }

    @GetMapping("/search/{invoiceNumber}")
    ApiResponse<Page<InvoiceDTO>> searchInvoice(@PathVariable String invoiceNumber, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return invoiceService.searchInvoices(invoiceNumber, page, size);
    }

    @GetMapping("/{invoiceNumber}")
    ApiResponse<InvoiceDTO> getInvoice(@PathVariable String invoiceNumber) {
        return invoiceService.getInvoice(invoiceNumber);
    }


    @GetMapping("/{invoiceNumber}/download")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable String invoiceNumber) {

        byte[] pdf = invoiceService.downloadInvoicePdf(invoiceNumber);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + invoiceNumber + ".pdf\"")
                .body(pdf);
    }

    @PutMapping("/update/{invoiceNumber}")
    ApiResponse<InvoiceDTO> updateInvoice(@PathVariable String invoiceNumber, @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.updateInvoiceStatus(invoiceNumber, invoiceDTO);
    }

    @DeleteMapping("/delete/{invoiceNumber}")
    void deleteInvoice(@PathVariable String invoiceNumber) {
         invoiceService.deleteInvoice(invoiceNumber);
    }
}
