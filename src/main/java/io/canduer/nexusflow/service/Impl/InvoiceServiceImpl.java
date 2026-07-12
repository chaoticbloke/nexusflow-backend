package io.canduer.nexusflow.service.Impl;

import io.canduer.nexusflow.dto.ApiResponse;
import io.canduer.nexusflow.dto.InvoiceDTO;
import io.canduer.nexusflow.dto.events.InvoiceCreatedEvent;
import io.canduer.nexusflow.entity.Customer;
import io.canduer.nexusflow.entity.Invoice;
import io.canduer.nexusflow.enums.InvoiceStatus;
import io.canduer.nexusflow.exception.ResourceNotFoundException;
import io.canduer.nexusflow.mapper.InvoiceMapper;
import io.canduer.nexusflow.repository.CustomerRepository;
import io.canduer.nexusflow.repository.InvoiceRepository;
import io.canduer.nexusflow.service.Impl.kafka.KafkaNotificationProducer;
import io.canduer.nexusflow.service.InvoiceService;
import io.canduer.nexusflow.service.InvoicePdfService;
import io.canduer.nexusflow.utils.IdentifierUUIDGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceMapper invoiceMapper;
    private final InvoiceRepository invoiceRepository;

    private final CustomerRepository customerRepository;

    private final NotificationService notificationService;
    private final KafkaNotificationProducer kafkaNotificationProducer;
    private final IdentifierUUIDGenerator identifierUUIDGenerator;
    private final InvoicePdfService pdfService;

    @Override
    @Transactional
    public ApiResponse<InvoiceDTO> createInvoice(InvoiceDTO invoiceDTO, String customerId) {
       Invoice invoice = invoiceMapper.invoiceDtoToInvoiceEntity(invoiceDTO);
        invoice.setInvoiceNumber(identifierUUIDGenerator.generateInvoiceNumber());
      Customer customer = customerRepository.findByCustomerId(customerId)
              .orElseThrow(()-> new ResourceNotFoundException("Customer not found for this id"));

       invoice.setCustomer(customer);
       notificationService.sendInvoiceEmail(customer.getEmail());
       log.info("Invoice created successfully");

       //kafka event
        InvoiceCreatedEvent event = InvoiceCreatedEvent.builder()
                .customerId(customerId)
                .invoiceNumber(invoice.getInvoiceNumber())
                .build();
        kafkaNotificationProducer.publishInvoiceCreatedV2(event);

       return ApiResponse.<InvoiceDTO>builder()
               .message("Invoice Created Successfully")
               .success(true)
               .data(invoiceMapper.invoiceEntityToInvoiceDto(invoiceRepository.save(invoice))).build();
    }

    @Override
    public ApiResponse<InvoiceDTO> getInvoice(String invoiceNumber) {
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber).orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        return ApiResponse.<InvoiceDTO>builder()
                .message("Invoice fetched Successfully")
                .success(true)
                .data(invoiceMapper.invoiceEntityToInvoiceDto(invoice)).build();
    }

    @Override
    public ApiResponse<Page<InvoiceDTO>> getInvoices(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());

        return ApiResponse.<Page<InvoiceDTO>>builder()
                        .data(invoiceRepository.findAll(pageRequest).map(invoiceMapper::invoiceEntityToInvoiceDto)).build();

    }

    @Override
    public ApiResponse<Page<InvoiceDTO>> getCustomerInvoices(String customerId, int pageNumber, int pageSize) {
       customerRepository.findByCustomerId(customerId).orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
       Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<InvoiceDTO> invoicePage = invoiceRepository.findByCustomerCustomerId(customerId, pageable).map(invoiceMapper::invoiceEntityToInvoiceDto);
        return ApiResponse.<Page<InvoiceDTO>>builder()
                .success(true)
                .message("Invoices fetched successfully for customer")
                .data(invoicePage)
                .build();
    }

    @Override
    public ApiResponse<Page<InvoiceDTO>> searchInvoices(String invoiceNumber, int pageNumber, int pageSize) {
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Invoice> pageInvoice = invoiceRepository.findByInvoiceNumberContainingIgnoreCase(invoiceNumber, pageRequest);
        return ApiResponse.<Page<InvoiceDTO>>builder()
                .message("Invoices fetched successfully")
                .success(true)
                .data(pageInvoice.map(invoiceMapper::invoiceEntityToInvoiceDto)).build();
    }

    @Override
    public ApiResponse<InvoiceDTO> updateInvoiceStatus(String invoiceNumber, String status) {
       Invoice invoice =  invoiceRepository.findByInvoiceNumber(invoiceNumber)
               .orElseThrow(()->new ResourceNotFoundException("Invoice not found :"+invoiceNumber));
       invoice.setStatus(InvoiceStatus.valueOf(status));
      return ApiResponse.<InvoiceDTO>builder()
                      .message("Invoice updated successfully")
                      .success(true)
                      .data(invoiceMapper.invoiceEntityToInvoiceDto(invoiceRepository.save(invoice))).build();
    }

    @Override
    public void deleteInvoice(String invoiceNumber) {
        Invoice invoice = invoiceRepository
                .findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found"));

        invoiceRepository.delete(invoice);
    }

    @Override
    public byte[] downloadInvoicePdf(String invoiceNumber) {
     Invoice invoice  =  invoiceRepository.findByInvoiceNumber(invoiceNumber)
             .orElseThrow(()-> new ResourceNotFoundException("Invoice not found "+invoiceNumber));

     //TODO: validate owner

       return pdfService.generateInvoicePdf(invoice);
    }
}
