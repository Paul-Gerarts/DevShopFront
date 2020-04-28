package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDtoListDto;
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

    public ProductDtoListDto convertToProductListDto(ProductListDto productListDto) {
        List<ProductDto> productDtoList = productListDto.getProducts().stream()
                .map(this::convertToProductDto)
                .map(this::setProductCountInProductDto)
                .collect(Collectors.toList());
        return new ProductDtoListDto(productDtoList);
    }

    private ProductDto setProductCountInProductDto(ProductDto productDto) {
        productDto.setTotalInCart(getProductCountFormCart(productDto.getName()));
        return productDto;
    }

    private int getProductCountFormCart(String productName) {
        return cartService.getCart().getProducts()
                .stream().filter(p -> p.getName().equals(productName))
                .findFirst()
                .map(Product::getTotalInCart)
                .orElse(0);
    }

}
