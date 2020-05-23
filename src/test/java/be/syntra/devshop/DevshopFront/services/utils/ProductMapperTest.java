package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductsDisplayListDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.CartUtils.getCartWithOneDummyProduct;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyNonArchivedProduct;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    @InjectMocks
    private ProductMapper productMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CartService cartService;

    @Test
    void convertToProductDtoTest() {
        // given
        Product product = getDummyNonArchivedProduct();
        List<String> categoryNames = List.of("Headphones");
        when(categoryMapper.mapToCategoryNames(product.getCategories())).thenReturn(categoryNames);

        // when
        ProductDto mappedProductDto = productMapper.convertToProductDto(product);

        // then
        assertEquals(mappedProductDto.getClass(), ProductDto.class);
        assertEquals(mappedProductDto.getName(), product.getName());
        assertEquals(mappedProductDto.getPrice(), product.getPrice());
        assertEquals(mappedProductDto.getDescription(), product.getDescription());
        assertEquals(mappedProductDto.isArchived(), product.isArchived());
        assertEquals(mappedProductDto.getCategoryNames().size(), product.getCategories().size());
        assertEquals(mappedProductDto.getCategoryNames().get(0), product.getCategories().get(0).getName());
    }

    @Test
    void convertToProductsDisplayListDto() {
        // given
        final ProductList dummyProductList = getDummyProductList();
        final CartDto dummyCartDto = getCartWithOneDummyProduct();
        when(cartService.getCart()).thenReturn(dummyCartDto);


        // when
        ProductsDisplayListDto productsDisplayList = productMapper.convertToProductsDisplayListDto(dummyProductList);

        // then
        assertEquals(productsDisplayList.getClass(), ProductsDisplayListDto.class);
        assertEquals(productsDisplayList.getProducts().size(), dummyProductList.getProducts().size());
        assertEquals(productsDisplayList.getProducts().get(0).getTotalInCart(), dummyProductList.getProducts().get(0).getTotalInCart());
    }
}