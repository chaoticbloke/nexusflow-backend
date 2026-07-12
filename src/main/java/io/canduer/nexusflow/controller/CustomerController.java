package io.canduer.nexusflow.controller;

import io.canduer.nexusflow.dto.*;
import io.canduer.nexusflow.entity.Customer;
import io.canduer.nexusflow.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("")
    ResponseEntity<ApiResponse<Page<CustomerResponseDTO>>> getCustomers(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                             @RequestParam(name = "size", defaultValue = "3") int pageSize){
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getCustomers(pageNumber, pageSize));
    }

    @PostMapping("/create")
    ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@RequestBody CreateCustomerRequestDTO customer){
        return  ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
    }

    @GetMapping("/search")
    ResponseEntity<ApiResponse<Page<Customer>>> search(@RequestParam String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<Customer>>builder().data(customerService.searchCustomers(name, page, pageSize)).build());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomer(@PathVariable String customerId) {

        CustomerResponseDTO customer = customerService.getCustomer(customerId);

        return ResponseEntity.ok(ApiResponse.<CustomerResponseDTO>builder().success(true).data(customer).build());
    }

    @GetMapping("/{customerId}/invoices")
    public ResponseEntity<ApiResponse< List<InvoiceDTO>>> getCustomerInvoices(@PathVariable String customerId) {

        List<InvoiceDTO> invoices = customerService.getCustomerInvoices(customerId);

        return ResponseEntity.ok(ApiResponse.< List<InvoiceDTO>>builder().success(true).data(invoices).build());
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomer(@PathVariable @NotBlank String customerId, @Valid @RequestBody UpdateCustomerRequestDTO updateCustomerRequestDTO) {

        CustomerResponseDTO customer = customerService.updateCustomer(customerId , updateCustomerRequestDTO);

        return ResponseEntity.ok(ApiResponse.<CustomerResponseDTO>builder().success(true).data(customer).message("Customer updated successfully")
                .build());
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {

        customerService.deleteCustomer(customerId);

        return ResponseEntity.noContent().build();
    }
}