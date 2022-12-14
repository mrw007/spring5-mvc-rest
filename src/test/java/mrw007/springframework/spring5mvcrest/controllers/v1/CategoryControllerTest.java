package mrw007.springframework.spring5mvcrest.controllers.v1;

import mrw007.springframework.spring5mvcrest.api.v1.model.CategoryDTO;
import mrw007.springframework.spring5mvcrest.services.CategoryService;
import mrw007.springframework.spring5mvcrest.services.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
class CategoryControllerTest {

    public static final String CATEGORY_NAME_1 = "Jim";
    public static final long ID_1 = 1L;
    public static final long ID_2 = 2L;
    public static final String CATEGORY_NAME_2 = "Bob";
    public static final String CATEGORIES_BASE_URL = "/api/v1/categories/";

    @Mock
    CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void testListCategories() throws Exception {

        CategoryDTO category1 = new CategoryDTO();
        category1.setId(ID_1);
        category1.setName(CATEGORY_NAME_1);

        CategoryDTO category2 = new CategoryDTO();
        category2.setId(ID_2);
        category2.setName(CATEGORY_NAME_2);

        List<CategoryDTO> categories = Arrays.asList(category1, category2);

        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get(CATEGORIES_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(2)));
    }

    @Test
    void testGetCategoryByName() throws Exception {
        CategoryDTO category = new CategoryDTO();
        category.setId(ID_1);
        category.setName(CATEGORY_NAME_1);

        when(categoryService.getCategoryByName(anyString())).thenReturn(category);

        mockMvc.perform(get(CATEGORIES_BASE_URL + CATEGORY_NAME_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(CATEGORY_NAME_1)));
    }

    @Test
    public void testGetByNameNotFound() throws Exception {

        when(categoryService.getCategoryByName(anyString())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CATEGORIES_BASE_URL + "/Foo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}