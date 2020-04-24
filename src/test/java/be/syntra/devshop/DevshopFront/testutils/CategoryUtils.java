package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.Category;

import java.util.List;

public class CategoryUtils {

    public static List<Category> createCategoryList() {
        return List.of(Category.builder()
                .name("Headphones")
                .build());
    }
}
