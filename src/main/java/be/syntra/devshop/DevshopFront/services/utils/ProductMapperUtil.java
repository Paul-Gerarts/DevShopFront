package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;

public class ProductMapperUtil {

    public static ProductDto convertToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .archived(product.isArchived())
                .build();
    }
}
