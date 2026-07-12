package io.canduer.nexusflow.service;

import io.canduer.nexusflow.dto.*;
import io.canduer.nexusflow.entity.Customer;
import io.canduer.nexusflow.entity.Invoice;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {

    //customer methods
    ApiResponse<CustomerResponseDTO> createCustomer(CreateCustomerRequestDTO customer);
    CustomerResponseDTO updateCustomer(String customerId, UpdateCustomerRequestDTO createCustomerRequestDTO);
    ApiResponse<Page<CustomerResponseDTO>> getCustomers(int page, int pageSize);
    Page<Customer> searchCustomers(String name, int page, int pageSize);


    //invoices methods
    Invoice createInvoice(Invoice invoice);
    void addInvoiceToCustomer(Long customerId, Invoice invoice);
    Page<Invoice> getInvoices(int page , int pageSize);

    CustomerResponseDTO getCustomer(String customerId);

    void deleteCustomer(String customerId);

    List<InvoiceDTO> getCustomerInvoices(String customerId);
}
