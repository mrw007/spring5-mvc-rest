package mrw007.springframework.spring5mvcrest.services;

import mrw007.springframework.spring5mvcrest.api.v1.model.CustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();

    CustomerDTO getCustomerByID(Long id);

    CustomerDTO createNewCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomerById(Long id, CustomerDTO customerDTO);
    CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO);
}
