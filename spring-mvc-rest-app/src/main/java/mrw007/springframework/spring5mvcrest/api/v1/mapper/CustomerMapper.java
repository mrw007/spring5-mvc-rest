package mrw007.springframework.spring5mvcrest.api.v1.mapper;

import mrw007.springframework.model.CustomerDTO;
import mrw007.springframework.spring5mvcrest.models.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerDTO customerToCustomerDTO(Customer customer);
    Customer customerDTOToCustomer(CustomerDTO customerDTO);
}
