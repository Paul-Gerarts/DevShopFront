package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductMapperUtil {

    @Autowired
    private CategoryMapperUtil categoryMapperUtil;

    public ProductDto convertToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .archived(product.isArchived())
                .totalInCart(product.getTotalInCart())
                .categoryNames(categoryMapperUtil.mapToCategoryNames(product.getCategories()))
                .build();
    }
}
