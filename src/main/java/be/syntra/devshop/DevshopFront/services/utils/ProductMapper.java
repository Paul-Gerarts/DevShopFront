package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductsDisplayListDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private CategoryMapper categoryMapper;

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
                .categoryNames(categoryMapper.mapToCategoryNames(product.getCategories()))
                .averageRating(product.getAverageRating())
                .ratings(product.getRatings())
                .build();
    }

    public ProductsDisplayListDto convertToProductsDisplayListDto(ProductList productList) {
        List<ProductDto> productDtoList = productList.getProducts().stream()
                .map(this::convertToProductDto)
                .map(this::setProductCountInProductDto)
                .collect(Collectors.toList());
        //return new ProductsDisplayListDto(productDtoList);
        return ProductsDisplayListDto.builder()
                .products(productDtoList)
                .hasNext(productList.isHasNext())
                .hasPrevious(productList.isHasPrevious())
                .currentPage(productList.getCurrentPage())
                .totalPages(productList.getTotalPages())
                .build();
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
