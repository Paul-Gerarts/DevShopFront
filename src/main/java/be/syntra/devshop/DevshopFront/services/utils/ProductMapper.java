package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductsDisplayListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    private final CategoryMapper categoryMapper;
    //private final CartService cartService;

    @Autowired
    public ProductMapper(
            CategoryMapper categoryMapper/*,
            CartService cartService*/
    ) {
        this.categoryMapper = categoryMapper;
        //this.cartService = cartService;
    }

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
                .reviews(product.getReviews())
                .build();
    }

    public Product convertToProduct(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .archived(productDto.isArchived())
                .totalInCart(productDto.getTotalInCart())
                .categories(categoryMapper.mapToCategories(productDto.getCategoryNames()))
                .averageRating(productDto.getAverageRating())
                .ratings(productDto.getRatings())
                .reviews(productDto.getReviews())
                .build();
    }

    public ProductsDisplayListDto convertToProductsDisplayListDto(ProductList productList) {
        List<ProductDto> productDtoList = productList.getProducts().stream()
                .map(this::convertToProductDto)
                .map(this::setProductCountInProductDto)
                .collect(Collectors.toList());
        return ProductsDisplayListDto.builder()
                .products(productDtoList)
                .hasNext(productList.isHasNext())
                .hasPrevious(productList.isHasPrevious())
                .currentPage(productList.getCurrentPage())
                .totalPages(productList.getTotalPages())
                .build();
    }

    public ProductDto convertToDisplayProductDto(Product product) {
        return setProductCountInProductDto(convertToProductDto(product));
    }

    private ProductDto setProductCountInProductDto(ProductDto productDto) {
        productDto.setTotalInCart(getCountFromProductInCart(productDto.getId()));
        return productDto;
    }

    private int getCountFromProductInCart(Long id) {
        /*return cartService.getCart().getProductDtos()
                .parallelStream().filter(p -> p.getId().equals(id))
                .findFirst()
                //.map(Product::getTotalInCart)
                .map(ProductDto::getTotalInCart)
                .orElse(0);*/

        return 0;
    }
}
