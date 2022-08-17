package mrw007.springframework.api.v1.mapper;

import mrw007.springframework.api.v1.model.CategoryDTO;
import mrw007.springframework.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO categoryToCategoryDTO(Category category);
}
