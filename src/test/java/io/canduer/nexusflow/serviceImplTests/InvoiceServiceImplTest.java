package io.canduer.nexusflow.serviceImplTests;

import io.canduer.nexusflow.dto.ApiResponse;
import io.canduer.nexusflow.dto.InvoiceDTO;
import io.canduer.nexusflow.entity.Customer;
import io.canduer.nexusflow.entity.Invoice;
import io.canduer.nexusflow.enums.InvoiceStatus;
import io.canduer.nexusflow.exception.ResourceNotFoundException;
import io.canduer.nexusflow.mapper.InvoiceMapper;
import io.canduer.nexusflow.repository.CustomerRepository;
import io.canduer.nexusflow.repository.InvoiceRepository;
import io.canduer.nexusflow.service.Impl.InvoiceServiceImpl;
import io.canduer.nexusflow.service.Impl.NotificationService;
import io.canduer.nexusflow.service.Impl.kafka.KafkaNotificationProducer;
import io.canduer.nexusflow.service.Impl.kafka.KafkaProducerService;
import io.canduer.nexusflow.utils.IdentifierUUIDGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceImplTest {

    @Mock
    InvoiceRepository invoiceRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    InvoiceMapper invoiceMapper;

    @InjectMocks
    InvoiceServiceImpl invoiceService;

    @Mock
    NotificationService notificationService;

    @Mock
    KafkaProducerService kafkaProducerService;

    @Mock
    private KafkaNotificationProducer kafkaNotificationProducer;

    @Mock
    IdentifierUUIDGenerator identifierUUIDGenerator;

    private final String customerId = "TEST_CUSTOMER_ID";

    @Test
    void createInvoiceCustomerNotFound() {

        //arrange
       // String customerId = "TEST_CUSTOMER_ID";
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        when(invoiceMapper.invoiceDtoToInvoiceEntity(invoiceDTO)).thenReturn(new Invoice());
        when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

        //act + assert
        assertThrows(ResourceNotFoundException.class, ()->{
           invoiceService.createInvoice(invoiceDTO, customerId);
        });
        verify(customerRepository).findByCustomerId(customerId);
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void createInvoiceSuccess() {
        //arrange : set up test data
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        when(invoiceMapper.invoiceDtoToInvoiceEntity(invoiceDTO)).thenReturn(getInvoice());
        when(customerRepository.findByCustomerId(customerId)).thenReturn(getCustomer());
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(getInvoice());
        when(invoiceMapper.invoiceEntityToInvoiceDto(any(Invoice.class))).thenReturn(getInvoiceDTO());

        //act: method call
        ApiResponse<InvoiceDTO> response = invoiceService.createInvoice(invoiceDTO, customerId);

        //assert : verify results
        assertNotNull(response);
        assertNotNull(response.getData());
        assertEquals(getInvoiceDTO(), response.getData());
        assertEquals("PENDING", response.getData().getStatus());
        assertTrue(response.isSuccess());
        assertEquals("Invoice Created Successfully" , response.getMessage());
        verify(customerRepository).findByCustomerId(customerId);
        verify(invoiceRepository).save(any());

    }

    @Test
    void getInvoiceSuccess1() {

        Invoice invoice = getInvoice();
        String invoiceNumber = "TEST_INVOICE_NUMBER";
        when(invoiceRepository.findByInvoiceNumber(invoiceNumber)).thenReturn(Optional.of(invoice));
        when(invoiceMapper.invoiceEntityToInvoiceDto(invoice)).thenReturn(getInvoiceDTO());
        ApiResponse<InvoiceDTO> response = invoiceService.getInvoice(invoiceNumber);

        assertNotNull(response);
        assertEquals(getInvoiceDTO(), response.getData());
        verify(invoiceRepository).findByInvoiceNumber(any());
    }

    @Test
    void getInvoiceSuccess() {

        String invoiceNumber = "TEST_INVOICE_NUMBER";

        Invoice invoice = getInvoice();
        InvoiceDTO dto = getInvoiceDTO();

        when(invoiceRepository.findByInvoiceNumber(invoiceNumber))
                .thenReturn(Optional.of(invoice));

        when(invoiceMapper.invoiceEntityToInvoiceDto(invoice))
                .thenReturn(dto);

        ApiResponse<InvoiceDTO> response =
                invoiceService.getInvoice(invoiceNumber);

        assertNotNull(response);
        assertNotNull(response.getData());

        verify(invoiceRepository)
                .findByInvoiceNumber(invoiceNumber);
    }
    @Test
    void getInvoiceFailure() {
        String invoiceNumber = "TEST_INVOICE_NUMBER";
        when(invoiceRepository.findByInvoiceNumber(invoiceNumber)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()->{
            invoiceService.getInvoice(invoiceNumber);
        });
    }
    private InvoiceDTO getInvoiceDTO() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setStatus("PENDING");
        invoiceDTO.setServices("TEST_SERVICE");
        return invoiceDTO;
    }

    private Invoice getInvoice() {
        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.valueOf("PENDING"));
        invoice.setCustomer(getCustomer().get());
        invoice.setServices("TEST_SERVICE");
        invoice.setTotal(BigDecimal.valueOf(12345.7));
        return invoice;
    }

    Optional<Customer> getCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId("12345");
        customer.setName("Aditya");
        return Optional.of(customer);
    }
}
