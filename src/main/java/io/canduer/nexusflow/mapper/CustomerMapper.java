package io.canduer.nexusflow.mapper;

import io.canduer.nexusflow.dto.CustomerResponseDTO;
import io.canduer.nexusflow.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

  CustomerResponseDTO customerEntityToCustomerDTO(Customer customer);
}
