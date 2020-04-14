package be.syntra.devshop.DevshopFront.TestUtils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public class ProductUtils {

    public static ProductDto getDummyProductDto() {
        return ProductDto.builder()
                .id(1L)
                .name("name")
                .price(new BigDecimal("55"))
                .description("description")
                .archived(false)
                .build();
    }

    public static ProductDto getOtherDummyProductDto() {
        return ProductDto.builder()
                .name("name of prod")
                .price(new BigDecimal("1.86"))
                .build();
    }

    public static Product getDummyNonArchivedProduct() {
        return Product.builder()
                .id(1L)
                .name("product")
                .price(new BigDecimal("88"))
                .description("description")
                .archived(false)
                .build();
    }

    public static Product getDummyArchivedProduct() {
        return Product.builder()
                .id(1L)
                .name("product")
                .price(new BigDecimal("88"))
                .description("description")
                .archived(true)
                .build();
    }

    public static Product getOtherDummyNonArchivedProduct() {
        return Product.builder()
                .id(2L)
                .name("another product")
                .price(new BigDecimal("0.99"))
                .archived(false)
                .build();
    }

    public static Product getOtherDummyArchivedProduct() {
        return Product.builder()
                .id(2L)
                .name("another product")
                .price(new BigDecimal("0.99"))
                .archived(true)
                .build();
    }

    public static List<ProductDto> getDummyProductDtoList() {
        return List.of(getDummyProductDto(), getOtherDummyProductDto());
    }

    public static List<Product> getDummyNonArchivedProductList() {
        return List.of(getDummyNonArchivedProduct(), getOtherDummyNonArchivedProduct());
    }

    public static List<Product> getDummyArchivedProductList() {
        return List.of(getDummyArchivedProduct(), getOtherDummyArchivedProduct());
    }
}
