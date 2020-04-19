package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.TestUtils.JsonUtils;
import be.syntra.devshop.DevshopFront.TestUtils.ProductUtils;
import be.syntra.devshop.DevshopFront.TestUtils.TestWebConfig;
import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RestClientTest(ProductListCacheServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
class ProductListCacheServiceImplTest {

    @Autowired
    private JsonUtils jsonUtils;

    @MockBean
    private ProductListCache productListCache;

    @MockBean
    private ProductService productService;

    @MockBean
    private DataStore dataStore;

    @Autowired
    ProductListCacheServiceImpl productListCacheService;

    @Test
    void updateProductListCache() {
        // given
        when(productService.findAllNonArchived()).thenReturn(new ProductList(ProductUtils.getDummyNonArchivedProductList()));

        // when
        productListCacheService.updateProductListCache();

        // then
        verify(productListCache).setProducts(anyList());
    }

    @Test
    void getProductListCache() {
        // given
        Map<String, Boolean> dummyMap = new HashMap<>();
        dummyMap.put("cacheNeedsUpdate", true);
        when(productService.findAllNonArchived()).thenReturn(new ProductList(ProductUtils.getDummyNonArchivedProductList()));
        when(dataStore.getMap()).thenReturn(dummyMap);

        // when
        productListCacheService.getProductListCache();

        // then
        verify(productService, times(1)).findAllNonArchived();
        verify(dataStore, times(2)).getMap();
    }

    @Test
    void findBySearchRequest() {
        // given
        Map<String, Boolean> dummyMap = new HashMap<>();
        dummyMap.put("cacheNeedsUpdate", true);
        when(productService.findAllNonArchived()).thenReturn(new ProductList(ProductUtils.getDummyNonArchivedProductList()));
        when(dataStore.getMap()).thenReturn(dummyMap);

        // when
        ProductList resultProductList = productListCacheService.findBySearchRequest(new SearchModel());

        // then
        assertEquals(resultProductList.getProducts().size(), 0);
    }
}