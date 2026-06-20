package io.canduer.nexusflow.service.Impl;

import io.canduer.nexusflow.dto.ApiResponse;
import io.canduer.nexusflow.dto.CreateCustomerRequestDTO;
import io.canduer.nexusflow.dto.CustomerResponseDTO;
import io.canduer.nexusflow.entity.Customer;
import io.canduer.nexusflow.entity.Invoice;
import io.canduer.nexusflow.repository.CustomerRepository;
import io.canduer.nexusflow.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    @CacheEvict(value = "customers", allEntries = true) //meaning - delete the cache on getcutoer to get the new data
    public ApiResponse<CustomerResponseDTO> createCustomer(CreateCustomerRequestDTO customerRequestDTO) {

        String customerId = "CUST-" + UUID.randomUUID()
                                .toString()
                                .substring(0, 8)
                                .toUpperCase();
        Customer customer = Customer.builder()
                .customerId(customerId)
                .name(customerRequestDTO.getName())
                .email(customerRequestDTO.getEmail())
                .address(customerRequestDTO.getAddress())
                .phone(customerRequestDTO.getPhone())
                .type(customerRequestDTO.getType())
                .build();


        String invoiceNumber = "INVOICE-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

        Invoice invoice = new Invoice();
        invoice.setCustomer(customer);
        invoice.setInvoiceNumber(invoiceNumber);
        invoice.setServices("TEST INVOICE");
        invoice.setStatus("PENDING");
        invoice.setTotal(1234.56);
        invoice.setDate(new Date());

        Invoice invoice1 = new Invoice();
        invoice1.setCustomer(customer);

        customer.setInvoices(Set.of(invoice, invoice1));
        Customer savedCustomer = customerRepository.save(customer);

        CustomerResponseDTO customerResponseDTO = CustomerResponseDTO.builder()
                .customerId(savedCustomer.getCustomerId())
                .name(savedCustomer.getName())
                .email(savedCustomer.getEmail())
                .type(savedCustomer.getType())
                .address(savedCustomer.getAddress())
                .phone(savedCustomer.getPhone())
                .build();
       return ApiResponse.<CustomerResponseDTO>builder()
               .success(true)
               .message("Customer created successfully")
                .data(customerResponseDTO)
                .build();
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return null;
    }

    @Override
    @Cacheable(
            value = "customers",
            key = "#pageNumber + '-' + #pageSize"
    )
    public Page<Customer> getCustomers(int pageNumber, int pageSize) {
        log.info("Fetching customers from DB");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        return customerRepository.findAll(pageRequest);
    }

    @Override
    public Page<Customer> searchCustomers(String name, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());
        return customerRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return null;
    }

    @Override
    public void addInvoiceToCustomer(Long customerId, Invoice invoice) {

    }

    @Override
    public Page<Invoice> getInvoices(int page, int pageSize) {
        return null;
    }
}
