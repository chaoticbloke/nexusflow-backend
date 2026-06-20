package io.canduer.nexusflow.controller;

import io.canduer.nexusflow.dto.ApiResponse;
import io.canduer.nexusflow.dto.CreateCustomerRequestDTO;
import io.canduer.nexusflow.dto.CustomerResponseDTO;
import io.canduer.nexusflow.entity.Customer;
import io.canduer.nexusflow.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("")
    ResponseEntity<ApiResponse<Page<Customer>>> getCustomers(@RequestParam(name = "page", defaultValue = "0") int pageNumber,
                                                             @RequestParam(name = "size", defaultValue = "10") int pageSize){
        return ResponseEntity.status(HttpStatus.OK).body( ApiResponse.<Page<Customer>>builder()
                .data(customerService.getCustomers(pageNumber, pageSize)).build());
    }

    @PostMapping("/create")
    ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@RequestBody CreateCustomerRequestDTO customer){
        return  ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
    }

    @GetMapping("/search")
    ResponseEntity<ApiResponse<Page<Customer>>> search(@RequestParam String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Page<Customer>>builder().data(customerService.searchCustomers(name, page, pageSize)).build());
    }
}