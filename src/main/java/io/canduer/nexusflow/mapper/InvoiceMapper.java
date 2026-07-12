package io.canduer.nexusflow.mapper;

import io.canduer.nexusflow.dto.InvoiceDTO;
import io.canduer.nexusflow.entity.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(target = "totalAmount", source = "total")
    @Mapping(target = "date", source = "dueDate")
    InvoiceDTO invoiceEntityToInvoiceDto(Invoice invoice);

    @Mapping(target = "total", source = "totalAmount")
    @Mapping(target = "dueDate", source = "date")
    Invoice invoiceDtoToInvoiceEntity(InvoiceDTO invoiceDTO);
}
