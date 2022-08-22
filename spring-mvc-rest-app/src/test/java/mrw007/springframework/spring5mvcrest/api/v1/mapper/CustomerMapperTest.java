package mrw007.springframework.spring5mvcrest.api.v1.mapper;

import mrw007.springframework.model.CustomerDTO;
import mrw007.springframework.spring5mvcrest.models.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    public static final long ID = 1L;
    public static final String CUSTOMER_FIRST_NAME = "Joe";
    public static final String CUSTOMER_LAST_NAME = "Newman";

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    @Test
    void customerToCustomerDTO() {
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstName(CUSTOMER_FIRST_NAME);
        customer.setLastName(CUSTOMER_LAST_NAME);

        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        assertEquals(CUSTOMER_FIRST_NAME, customerDTO.getFirstName());
        assertEquals(CUSTOMER_LAST_NAME, customerDTO.getLastName());
    }

    @Test
    void customerDTOToCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(CUSTOMER_FIRST_NAME);
        customerDTO.setLastName(CUSTOMER_LAST_NAME);

        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);

        assertEquals(CUSTOMER_FIRST_NAME, customer.getFirstName());
        assertEquals(CUSTOMER_LAST_NAME, customer.getLastName());

    }
}