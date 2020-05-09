package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductsDisplayList;
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

    public Product convertToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .archived(productDto.isArchived())
                .totalInCart(0)
                .categories(categoryMapperUtil.mapToCategories(productDto.getCategoryNames()))
                .build();

    }

    public ProductsDisplayList convertToProductDtoList(ProductList productList) {
        List<ProductDto> productDtoList = productList.getProducts().stream()
                .map(this::convertToProductDto)
                .map(this::setProductCountInProductDto)
                .collect(Collectors.toList());
        return new ProductsDisplayList(productDtoList);
    }

    private ProductDto setProductCountInProductDto(ProductDto productDto) {
        productDto.setTotalInCart(getCountFromProductInCart(productDto.getId()));
        return productDto;
    }

    private int getCountFromProductInCart(Long id) {
        return cartService.getCart().getProducts()
                .parallelStream().filter(p -> p.getId().equals(id))
                .findFirst()
                .map(Product::getTotalInCart)
                .orElse(0);
    }
}
