package mrw007.springframework.spring5mvcrest.services;

import mrw007.springframework.spring5mvcrest.api.v1.mapper.CustomerMapper;
import mrw007.springframework.spring5mvcrest.api.v1.model.CustomerDTO;
import mrw007.springframework.spring5mvcrest.models.Customer;
import mrw007.springframework.spring5mvcrest.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    public static final String CUSTOMERS_BASE_URL = "/api/v1/customers/";
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerUrl(CUSTOMERS_BASE_URL + customer.getId());
                    return customerDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerByID(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            //TODO Implement better exception Handling
            throw new RuntimeException("Customer with ID= " + id + " not Found!");
        }
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer.get());
        customerDTO.setCustomerUrl(CUSTOMERS_BASE_URL + customerDTO.getId());
        return customerDTO;
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        CustomerDTO resultedCustomerDTO = customerMapper.customerToCustomerDTO(savedCustomer);
        resultedCustomerDTO.setCustomerUrl(CUSTOMERS_BASE_URL + savedCustomer.getId());
        return resultedCustomerDTO;
    }
}
