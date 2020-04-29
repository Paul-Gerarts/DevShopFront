package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDtoList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductListDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapperUtil {

    @Autowired
    private CategoryMapperUtil categoryMapperUtil;

    @Autowired
    private CartService cartService;

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

    public ProductDtoList convertToProductDtoListDto(ProductListDto productListDto) {
        List<ProductDto> productDtoList = productListDto.getProducts().stream()
                .map(this::convertToProductDto)
                .map(this::setProductCountInProductDto)
                .collect(Collectors.toList());
        return new ProductDtoList(productDtoList);
    }

    private ProductDto setProductCountInProductDto(ProductDto productDto) {
        productDto.setTotalInCart(getCountFormProductInCart(productDto.getId()));
        return productDto;
    }

    private int getCountFormProductInCart(Long id) {
        return cartService.getCart().getProducts()
                .parallelStream().filter(p -> p.getId().equals(id))
                .findFirst()
                .map(Product::getTotalInCart)
                .orElse(0);
    }
}
