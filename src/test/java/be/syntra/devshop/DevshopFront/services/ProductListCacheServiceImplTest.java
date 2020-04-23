package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.testutils.JsonUtils;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyNonArchivedProductList;
import static org.assertj.core.api.Assertions.assertThat;
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
    private SearchService searchService;

    @MockBean
    private DataStore dataStore;

    @Autowired
    ProductListCacheServiceImpl productListCacheService;

    @Test
    void updateProductListCache() {
        // given
        when(productService.findAllNonArchived()).thenReturn(new ProductList(getDummyNonArchivedProductList()));

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
        when(productService.findAllNonArchived()).thenReturn(new ProductList(getDummyNonArchivedProductList()));
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
        when(productService.findAllNonArchived()).thenReturn(new ProductList(getDummyNonArchivedProductList()));
        when(dataStore.getMap()).thenReturn(dummyMap);

        // when
        ProductList resultProductList = productListCacheService.findBySearchRequest(new SearchModel());

        // then
        assertEquals(resultProductList.getProducts().size(), 0);
    }

    @Test
    void canSortProductByNameTest() {
        // given
        List<Product> dummyProductList = getDummyNonArchivedProductList();
        SearchModel dummySearchModel = new SearchModel();
        dummySearchModel.setSortAscendingName(true);

        // when
        ProductList resultProducts = productListCacheService.sortListByName(dummyProductList, dummySearchModel);

        // then
        assertThat(resultProducts.getProducts().get(0)).isEqualTo(dummyProductList.get(1));
    }

    @Test
    void canSortProductByPriceTest() {
        // given
        List<Product> dummyProductList = getDummyNonArchivedProductList();
        SearchModel dummySearchModel = new SearchModel();
        dummySearchModel.setSortAscendingPrice(true);

        // when
        ProductList resultProducts = productListCacheService.sortListByPrice(dummyProductList, dummySearchModel);

        // then
        assertThat(resultProducts.getProducts().get(0)).isEqualTo(dummyProductList.get(1));
    }

    @Test
    void canSetPriceFiltersTest() {
        // given
        List<Product> dummyProductList = getDummyNonArchivedProductList();
        BigDecimal priceHigh = new BigDecimal("10000");
        boolean originalSortAscendingPrice = false;
        SearchModel dummySearchModel = new SearchModel();
        dummySearchModel.setSortAscendingPrice(originalSortAscendingPrice);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);

        // when
        productListCacheService.setPriceFilters(dummyProductList);

        // then
        assertThat(priceHigh).isNotEqualTo(dummySearchModel.getPriceHigh());
        assertThat(originalSortAscendingPrice).isNotEqualTo(dummySearchModel.isSortAscendingPrice());
    }

    @Test
    void canFilterByPriceTest() {
        // given
        List<Product> dummyProductList = getDummyNonArchivedProductList();
        BigDecimal priceLow = BigDecimal.ZERO;
        BigDecimal priceHigh = BigDecimal.TEN;
        boolean originalSortAscendingPrice = false;
        SearchModel dummySearchModel = new SearchModel();
        dummySearchModel.setPriceLow(priceLow);
        dummySearchModel.setPriceHigh(priceHigh);
        dummySearchModel.setSortAscendingPrice(originalSortAscendingPrice);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);

        // when
        ProductList result = productListCacheService.filterByPrice(dummyProductList, dummySearchModel);

        // then
        assertThat(dummyProductList.size()).isNotEqualTo(result.getProducts().size());
        assertThat(result.getProducts().get(0).getPrice()).isBetween(priceLow, priceHigh);
    }

    @Test
    void canFilterByProductDescriptionTest() {
        // given
        Map<String, Boolean> dummyMap = new HashMap<>();
        dummyMap.put("cacheNeedsUpdate", true);
        List<Product> dummyProductList = getDummyNonArchivedProductList();
        when(productService.findAllNonArchived()).thenReturn(new ProductList(dummyProductList));
        when(dataStore.getMap()).thenReturn(dummyMap);
        BigDecimal priceLow = new BigDecimal("0");
        BigDecimal priceHigh = new BigDecimal("100000");
        boolean originalSortAscendingPrice = false;
        String description = "another";
        SearchModel dummySearchModel = new SearchModel();
        dummySearchModel.setPriceLow(priceLow);
        dummySearchModel.setPriceHigh(priceHigh);
        dummySearchModel.setDescription(description);
        dummySearchModel.setSortAscendingPrice(originalSortAscendingPrice);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);

        // when
        ProductList result = productListCacheService.searchForProductDescription(dummyProductList, dummySearchModel);

        // then
        assertThat(dummyProductList.size()).isNotEqualTo(result.getProducts().size());
        assertThat(result.getProducts().get(0).getDescription()).contains(description);
    }
}