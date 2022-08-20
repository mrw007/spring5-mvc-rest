package mrw007.springframework.spring5mvcrest.controllers.v1;


import mrw007.springframework.spring5mvcrest.api.v1.model.CustomerDTO;
import mrw007.springframework.spring5mvcrest.api.v1.model.CustomerListDTO;
import mrw007.springframework.spring5mvcrest.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CustomerController.CUSTOMERS_BASE_URL)
public class CustomerController {
    public static final String CUSTOMERS_BASE_URL = "/api/v1/customers";
    CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomerListDTO getAllCustomers() {
        return new CustomerListDTO(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerByID(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createNewCustomer(@RequestBody CustomerDTO customerDTO) {
        return customerService.createNewCustomer(customerDTO);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO updateCustomerById(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return customerService.updateCustomerById(id, customerDTO);
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO patchCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        return customerService.patchCustomer(id, customerDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
    }
}
