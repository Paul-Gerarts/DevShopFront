package be.syntra.devshop.DevshopFront.services.utils;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyNonArchivedProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class ProductMapperUtilTest {

    @InjectMocks
    private ProductMapperUtil productMapperUtil;

    @Mock
    private CategoryMapperUtil categoryMapperUtil;

    @BeforeEach
    public void setUp() {
        initMocks(this);
    }

    @Test
    void convertToProductDtoTest() {
        // given
        Product product = getDummyNonArchivedProduct();
        List<String> categoryNames = List.of("Headphones");
        when(categoryMapperUtil.mapToCategoryNames(product.getCategories())).thenReturn(categoryNames);

        // when
        ProductDto mappedProductDto = productMapperUtil.convertToProductDto(product);

        // then
        assertEquals(mappedProductDto.getClass(), ProductDto.class);
        assertEquals(mappedProductDto.getName(), product.getName());
        assertEquals(mappedProductDto.getPrice(), product.getPrice());
        assertEquals(mappedProductDto.getDescription(), product.getDescription());
        assertEquals(mappedProductDto.isArchived(), product.isArchived());
        assertEquals(mappedProductDto.getCategoryNames().size(), product.getCategories().size());
        assertEquals(mappedProductDto.getCategoryNames().get(0), product.getCategories().get(0).getName());
    }
}