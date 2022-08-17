package mrw007.springframework.api.v1.mapper;

import mrw007.springframework.api.v1.model.CategoryDTO;
import mrw007.springframework.spring5mvcrest.models.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryMapperTest {

    public static final long ID = 1L;
    public static final String CATEGORY_NAME = "Joe";
    CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    @Test
    void categoryToCategoryDto() {
        //given
        Category category = new Category();
        category.setName(CATEGORY_NAME);
        category.setId(ID);
        System.out.println(category);

        //when
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);
        System.out.println(categoryDTO);

        //then
        assertEquals(ID, categoryDTO.getId());
        assertEquals(CATEGORY_NAME, categoryDTO.getName());
    }
}