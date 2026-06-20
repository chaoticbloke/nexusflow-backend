package io.canduer.nexusflow.service;

import io.canduer.nexusflow.dto.ApiResponse;
import io.canduer.nexusflow.dto.CreateCustomerRequestDTO;
import io.canduer.nexusflow.dto.CustomerResponseDTO;
import io.canduer.nexusflow.entity.Customer;
import io.canduer.nexusflow.entity.Invoice;
import org.springframework.data.domain.Page;

public interface CustomerService {

    //customer methods
    ApiResponse<CustomerResponseDTO> createCustomer(CreateCustomerRequestDTO customer);
    Customer updateCustomer(Customer customer);
    Page<Customer> getCustomers(int page, int pageSize);
    Page<Customer> searchCustomers(String name, int page, int pageSize);


    //invoices methods
    Invoice createInvoice(Invoice invoice);
    void addInvoiceToCustomer(Long customerId, Invoice invoice);
    Page<Invoice> getInvoices(int page , int pageSize);
}
