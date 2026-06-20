package io.canduer.nexusflow.serviceImplTests;

import io.canduer.nexusflow.entity.Customer;
import io.canduer.nexusflow.repository.CustomerRepository;
import io.canduer.nexusflow.service.Impl.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    CustomerServiceImpl customerService;

    @Mock
    CustomerRepository customerRepository;


    @Test
    void customerSearchSuccess() {

        when(customerRepository.findByNameContainingIgnoreCase(eq("adi"), any(Pageable.class))).thenReturn(getCustomerPage());

        Page<Customer> result = customerService.searchCustomers("adi", 0, 10);

        assert(result.getContent().size() > 0);
        assertEquals(4, result.getContent().size());
        assertEquals("Aditya", result.getContent().get(0).getName());
        Mockito.verify(customerRepository).findByNameContainingIgnoreCase(eq("adi"), any(Pageable.class));
    }

    private Page<Customer> getCustomerPage() {

        Page<Customer> expectedPage = new PageImpl<>(List.of(Customer.builder().name("Aditya").build(), Customer.builder()
                .name("ADITYA").build(), Customer.builder().name("adi").build(), Customer.builder().name("fditya").build()));

        return expectedPage;
    }
}
