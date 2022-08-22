package mrw007.springframework.spring5mvcrest.repositories;

import mrw007.springframework.spring5mvcrest.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
