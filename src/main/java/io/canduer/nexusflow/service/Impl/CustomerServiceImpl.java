package io.canduer.nexusflow.service.Impl;

import io.canduer.nexusflow.dto.*;
import io.canduer.nexusflow.entity.Customer;
import io.canduer.nexusflow.entity.Invoice;
import io.canduer.nexusflow.entity.User;
import io.canduer.nexusflow.enums.CustomerType;
import io.canduer.nexusflow.exception.ResourceNotFoundException;
import io.canduer.nexusflow.mapper.CustomerMapper;
import io.canduer.nexusflow.mapper.InvoiceMapper;
import io.canduer.nexusflow.repository.CustomerRepository;
import io.canduer.nexusflow.repository.InvoiceRepository;
import io.canduer.nexusflow.service.CustomerService;
import io.canduer.nexusflow.utils.IdentifierUUIDGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final InvoiceRepository invoiceRepository;

    private final CurrentUserService currentUserService;

    private final IdentifierUUIDGenerator identifierUUIDGenerator;

    private final CustomerMapper customerMapper;

    private final InvoiceMapper invoiceMapper;

    @Override
    @Transactional
    @CacheEvict(value = "customers", allEntries = true) //meaning - delete the cache on getCustomer to get the new data
    public ApiResponse<CustomerResponseDTO> createCustomer(CreateCustomerRequestDTO customerRequestDTO) {

        log.info("createCustomer customerRequestDTO payload: {}", customerRequestDTO);
        User currentLoggedInUser = currentUserService.getCurrentUser();

        Customer customer = Customer.builder()
                .customerId(identifierUUIDGenerator.generateCustomerId())
                .name(customerRequestDTO.getName())
                .email(customerRequestDTO.getEmail())
                .address(customerRequestDTO.getAddress())
                .phone(customerRequestDTO.getPhone())
                .type(CustomerType.valueOf(customerRequestDTO.getType().toUpperCase()))
                .createdBy(currentLoggedInUser)
                .build();


        Customer savedCustomer = customerRepository.save(customer);
        log.info("saved customer in DB name : {}", savedCustomer.getName());
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
    @Transactional
    public CustomerResponseDTO updateCustomer(String customerId, UpdateCustomerRequestDTO updateCustomerRequestDTO) {
        log.info("Updating customer {}", customerId);
        Customer customer = customerRepository.findByCustomerId(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found for this id " + customerId));

        customer.setName(updateCustomerRequestDTO.getName());
        customer.setType(CustomerType.valueOf(updateCustomerRequestDTO.getType().toUpperCase()));
        customer.setAddress(updateCustomerRequestDTO.getAddress());
        customer.setEmail(updateCustomerRequestDTO.getEmail());
        customer.setPhone(updateCustomerRequestDTO.getPhone());

        Customer savedCustomer = customerRepository.save(customer); //hibernate managed entity - save is optional in here-JPA's dirty checking


        return customerMapper.customerEntityToCustomerDTO(savedCustomer);
    }

    @Override
    public ApiResponse<Page<CustomerResponseDTO>> getCustomers(int pageNumber, int pageSize) {
        log.info("Fetching customers from DB");
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<Customer> customers = customerRepository.findAll(pageRequest);
       return ApiResponse.<Page<CustomerResponseDTO>>builder()
               .data(customers.map(customerMapper::customerEntityToCustomerDTO))
               .build();
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

    @Override
    public CustomerResponseDTO getCustomer(String customerId) {
        Customer customer = customerRepository.findByCustomerId(customerId).orElseThrow(()->new ResourceNotFoundException("Customer not found for this customerId "+ customerId));
        return customerMapper.customerEntityToCustomerDTO(customer);
    }

    @Override
    public void deleteCustomer(String customerId) {
        Customer customer = customerRepository
                .findByCustomerId(customerId)
                .orElseThrow(()->new ResourceNotFoundException("Customer not found for this customerId "+ customerId));

        customerRepository.delete(customer);
    }

    @Override
    public List<InvoiceDTO> getCustomerInvoices(String customerId) {

        customerRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id: " + customerId));

        return invoiceRepository
                .findByCustomerCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(invoiceMapper::invoiceEntityToInvoiceDto)
                .toList();
    }
}
