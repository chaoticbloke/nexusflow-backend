package io.canduer.nexusflow.repository;

import io.canduer.nexusflow.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Page<Invoice> findByInvoiceNumberContainingIgnoreCase(String invoiceNumber, Pageable pageable);

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber );

    Page<Invoice> findByCustomerCustomerId(String customerId, Pageable pageable);
}
