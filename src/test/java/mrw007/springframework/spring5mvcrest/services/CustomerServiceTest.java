package mrw007.springframework.spring5mvcrest.services;

import mrw007.springframework.spring5mvcrest.api.v1.mapper.CustomerMapper;
import mrw007.springframework.spring5mvcrest.api.v1.model.CustomerDTO;
import mrw007.springframework.spring5mvcrest.models.Customer;
import mrw007.springframework.spring5mvcrest.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class CustomerServiceTest {
    public static final long ID = 1L;
    public static final String CUSTOMER_FIRST_NAME = "Joe";
    public static final String CUSTOMER_LAST_NAME = "Newman";
    public static final String CUSTOMERS_BASE_URL = "/api/v1/customers/";


    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE, customerRepository);
    }

    @Test
    void getAllCustomers() {
        List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());
        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerDTO> customerDTOS = customerService.getAllCustomers();

        assertEquals(3, customerDTOS.size());
    }

    @Test
    void getCustomerByID() {
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstName(CUSTOMER_FIRST_NAME);
        customer.setLastName(CUSTOMER_LAST_NAME);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        CustomerDTO customerDTO = customerService.getCustomerByID(ID);

        assertEquals(ID, customerDTO.getId());
        assertEquals(CUSTOMER_FIRST_NAME, customerDTO.getFirstName());
        assertEquals(CUSTOMER_LAST_NAME, customerDTO.getLastName());
        assertEquals(CUSTOMERS_BASE_URL + ID, customerDTO.getCustomerUrl());
    }

    @Test
    void createNewCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(CUSTOMER_FIRST_NAME);
        customerDTO.setLastName(CUSTOMER_LAST_NAME);

        Customer savedCustomer = new Customer();
        savedCustomer.setId(ID);
        savedCustomer.setFirstName(customerDTO.getFirstName());
        savedCustomer.setLastName(customerDTO.getLastName());

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerDTO savedDto = customerService.createNewCustomer(customerDTO);

        assertEquals(customerDTO.getFirstName(), savedDto.getFirstName());
        assertEquals(CUSTOMERS_BASE_URL + ID, savedDto.getCustomerUrl());
    }
}