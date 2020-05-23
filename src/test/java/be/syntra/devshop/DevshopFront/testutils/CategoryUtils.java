package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.Category;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryChangeDto;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryDto;

import java.util.List;

public class CategoryUtils {

    public static List<Category> createCategoryList() {
        return List.of(Category.builder()
                        .id(1L)
                        .name("Headphones")
                        .build(),
                Category.builder()
                        .id(1L)
                        .name("Office")
                        .build());
    }

    public static CategoryDto createCategoryDto() {
        return CategoryDto.builder()
                .id(1L)
                .name("Headphones")
                .build();
    }

    public static CategoryChangeDto createCategoryChangeDto() {
        return CategoryChangeDto.builder()
                .categoryToDelete(1L)
                .categoryToSet(2L)
                .newCategoryName("Test")
                .build();
    }
}
