package mrw007.springframework.spring5mvcrest.repositories;

import mrw007.springframework.spring5mvcrest.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
