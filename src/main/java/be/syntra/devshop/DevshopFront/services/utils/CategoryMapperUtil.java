package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapperUtil {

    public List<String> mapToCategoryNames(List<Category> categories) {
        List<String> categoryNames = new ArrayList<>();
        categories.forEach(category -> categoryNames.add(category.getName()));
        return categoryNames;
    }

    public List<Category> mapToCategories(List<String> categoryStrings) {
        List<Category> categories = categoryStrings.stream().map(categoryString -> Category.builder().name(categoryString).build()).collect(Collectors.toList());
        return categories;
    }

}
