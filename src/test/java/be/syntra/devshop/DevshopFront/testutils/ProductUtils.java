package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductListAndMinMaxPrice;
import be.syntra.devshop.DevshopFront.models.dtos.ProductsDisplayList;

import java.math.BigDecimal;
import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.CategoryUtils.createCategoryList;

public class ProductUtils {

    public static ProductDto getDummyProductDto() {
        return ProductDto.builder()
                .id(1L)
                .name("product")
                .price(new BigDecimal("88"))
                .description("description")
                .archived(false)
                .categoryNames(List.of("Headphones"))
                .build();
    }

    public static ProductDto getOtherDummyProductDto() {
        return ProductDto.builder()
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
                .totalInCart(1)
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
                .totalInCart(1)
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

    public static ProductsDisplayList getDummyProductDtoList() {
        return new ProductsDisplayList(getDummyListOfProductDtos());
    }

    public static List<ProductDto> getDummyListOfProductDtos() {
        return (List.of(getDummyProductDto(), getOtherDummyProductDto()));
    }

    public static List<Product> getDummyNonArchivedProductList() {
        return List.of(getDummyNonArchivedProduct(), getOtherDummyNonArchivedProduct());
    }

    public static List<Product> getDummyArchivedProductList() {
        return List.of(getDummyArchivedProduct(), getOtherDummyArchivedProduct());
    }

    public static List<Product> getDummyAllProductList() {
        return List.of(getDummyNonArchivedProduct(), getDummyArchivedProduct());
    }

    public static ProductListAndMinMaxPrice getDummyProductListAndMinMaxPrice() {
        return ProductListAndMinMaxPrice.builder()
                .products(getDummyNonArchivedProductList())
                .searchResultMinPrice(BigDecimal.ZERO)
                .searchResultMaxPrice(BigDecimal.TEN)
                .build();
    }
}