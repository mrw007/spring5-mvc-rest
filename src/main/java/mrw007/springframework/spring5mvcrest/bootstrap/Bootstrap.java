package mrw007.springframework.spring5mvcrest.bootstrap;

import mrw007.springframework.spring5mvcrest.models.Category;
import mrw007.springframework.spring5mvcrest.models.Customer;
import mrw007.springframework.spring5mvcrest.models.Vendor;
import mrw007.springframework.spring5mvcrest.repositories.CategoryRepository;
import mrw007.springframework.spring5mvcrest.repositories.CustomerRepository;
import mrw007.springframework.spring5mvcrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        loadCategories();
        System.out.println("Categories Data loaded = " + categoryRepository.count());

        loadCustomers();
        System.out.println("Customers Data loaded = " + customerRepository.count());

        loadVendors();
        System.out.println("Vendors Data loaded = " + customerRepository.count());
    }

    private void loadVendors() {
        Vendor vendor1 = new Vendor();
        vendor1.setName("Vendor 1");

        Vendor vendor2 = new Vendor();
        vendor2.setName("Vendor 2");

        vendorRepository.save(vendor1);
        vendorRepository.save(vendor2);
    }

    private void loadCustomers() {
        Customer joe = new Customer();
        joe.setFirstName("Joe");
        joe.setLastName("Newman");

        Customer mary = new Customer();
        mary.setFirstName("Mary");
        mary.setLastName("Lachappele");

        Customer suzy = new Customer();
        suzy.setFirstName("Suzy");
        suzy.setLastName("Woodard");

        Customer stuart = new Customer();
        stuart.setFirstName("Stuart");
        stuart.setLastName("Jordon");

        Customer anne = new Customer();
        anne.setFirstName("Anne");
        anne.setLastName("Evans");

        Customer austin = new Customer();
        austin.setFirstName("Austin");
        austin.setLastName("Evans");

        customerRepository.save(anne);
        customerRepository.save(austin);
        customerRepository.save(mary);
        customerRepository.save(joe);
        customerRepository.save(stuart);
        customerRepository.save(suzy);
    }

    private void loadCategories() {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.save(fruits);
        categoryRepository.save(dried);
        categoryRepository.save(fresh);
        categoryRepository.save(exotic);
        categoryRepository.save(nuts);
    }
}
