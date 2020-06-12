package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductsDisplayListDto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.CategoryUtils.createCategoryList;
import static be.syntra.devshop.DevshopFront.testutils.ReviewUtils.getReviewSetWithDummyReview;

public class ProductUtils {

    public static ProductDto getDummyProductDto() {
        return ProductDto.builder()
                .id(1L)
                .name("product")
                .price(new BigDecimal("88"))
                .description("description")
                .archived(false)
                .categoryNames(List.of("Headphones"))
                .averageRating(0D)
                .ratings(Collections.emptySet())
                .reviews(Collections.emptySet())
                .build();
    }

    public static ProductDto getOtherDummyProductDto() {
        return ProductDto.builder()
                .id(1L)
                .name("name of prod")
                .price(new BigDecimal("1.86"))
                .categoryNames(List.of("Headphones"))
                .build();
    }

    public static Product getDummyNonArchivedProduct() {
        return Product.builder()
                .id(1L)
                .name("product")
                .price(new BigDecimal("88"))
                .description("description")
                .archived(false)
                .categories(createCategoryList())
                .averageRating(0D)
                .ratings(Collections.emptySet())
                .reviews(Collections.emptySet())
                .build();
    }

    public static Product getDummyProductWithReview() {
        return Product.builder()
                .id(1L)
                .name("product")
                .price(new BigDecimal("88"))
                .description("description")
                .archived(false)
                .categories(createCategoryList())
                .averageRating(0D)
                .ratings(Collections.emptySet())
                .reviews(getReviewSetWithDummyReview())
                .build();
    }

    public static ProductDto getDummyProductDtoWithReview() {
        return ProductDto.builder()
                .id(1L)
                .name("product")
                .price(new BigDecimal("88"))
                .description("description")
                .archived(false)
                .categoryNames(List.of("Headphones"))
                .averageRating(0D)
                .ratings(Collections.emptySet())
                .reviews(getReviewSetWithDummyReview())
                .build();
    }

    public static Product getDummyArchivedProduct() {
        return Product.builder()
                .id(1L)
                .name("product")
                .price(new BigDecimal("88"))
                .description("description")
                .archived(true)
                .categories(createCategoryList())
                .build();
    }

    public static Product getOtherDummyNonArchivedProduct() {
        return Product.builder()
                .id(2L)
                .name("another product")
                .price(new BigDecimal("0.99"))
                .description("another description")
                .archived(false)
                .categories(createCategoryList())
                .build();
    }

    public static Product getOtherDummyArchivedProduct() {
        return Product.builder()
                .id(2L)
                .name("another product")
                .price(new BigDecimal("0.99"))
                .description("another description")
                .archived(true)
                .categories(createCategoryList())
                .build();
    }

    public static ProductsDisplayListDto getDummyProductDtoList() {
        return ProductsDisplayListDto.builder().products(getDummyListOfProductDtos()).build();
    }

    public static List<ProductDto> getDummyListOfProductDtos() {
        return (List.of(getDummyProductDto(), getOtherDummyProductDto()));
    }

    public static List<Product> getDummyNonArchivedProductList() {
        return List.of(getDummyNonArchivedProduct(), getOtherDummyNonArchivedProduct());
    }

    public static List<Product> getDummyAllProductList() {
        return List.of(getDummyNonArchivedProduct(), getDummyArchivedProduct());
    }

    public static ProductList getDummyProductList() {
        return ProductList.builder()
                .products(getDummyNonArchivedProductList())
                .searchResultMinPrice(BigDecimal.ZERO)
                .searchResultMaxPrice(BigDecimal.TEN)
                .currentPage(1)
                .hasNext(true)
                .hasPrevious(true)
                .totalPages(3)
                .build();
    }

    public static ProductList getDummyEmptyProductList() {
        return ProductList.builder()
                .products(Collections.emptyList())
                .searchResultMinPrice(BigDecimal.ZERO)
                .searchResultMaxPrice(BigDecimal.TEN)
                .currentPage(1)
                .hasNext(true)
                .hasPrevious(true)
                .totalPages(3)
                .build();
    }
}