package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Category;
import org.junit.jupiter.api.Test;

import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.CategoryUtils.createCategoryList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryMapperTest {

    private final CategoryMapper categoryMapper = new CategoryMapper();

    @Test
    void canMapToCategoryNamesTest() {
        // given
        List<Category> categories = createCategoryList();

        // when
        List<String> result = categoryMapper.mapToCategoryNames(categories);

        // then
        assertThat(result.size()).isEqualTo(categories.size());
        assertThat(result.get(0)).isEqualTo(categories.get(0).getName());
    }
}
