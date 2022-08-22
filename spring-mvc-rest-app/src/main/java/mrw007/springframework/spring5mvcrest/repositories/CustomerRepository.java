package mrw007.springframework.spring5mvcrest.repositories;

import mrw007.springframework.spring5mvcrest.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
