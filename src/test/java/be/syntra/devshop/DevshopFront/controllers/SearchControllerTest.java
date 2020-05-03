package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductListDto;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.testutils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.CartUtils.*;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyArchivedProductList;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyNonArchivedProductList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SearchController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
public class SearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    /*@MockBean
    private ProductListCacheService productListCacheService;*/

    @MockBean
    private CartService cartService;

    @MockBean
    private SearchService searchService;

    @Test
    public void canDisplaySearchedForProductsTest() throws Exception {
        // given
        final String searchRequest = "product";
        final String description = "another";
        final List<Product> dummyProducts = getDummyNonArchivedProductList();
        final ProductListDto dummyProductListDto = new ProductListDto(dummyProducts);
        final CartDto dummyCart = getCartWithOneDummyProduct();
        final SearchModel dummySearchModel = new SearchModel();
        dummySearchModel.setSearchRequest(searchRequest);
        dummySearchModel.setDescription(description);

        when(productService.findBySearchRequest(any())).thenReturn(dummyProductListDto);
        when(productService.filterByPrice(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(productService.searchForProductDescription(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(cartService.getCart()).thenReturn(dummyCart);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/?searchRequest=" + searchRequest));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", dummyProducts))
                .andExpect(model().attribute("cart", dummyCart));

        verify(productService, times(1)).findBySearchRequest(any());
        verify(productService, times(1)).filterByPrice(any(), any());
    }

    @Test
    void canSearchForDescriptionTest() throws Exception {
        // given
        final String searchRequest = "product";
        final String description = "another";
        final List<Product> dummyProducts = getDummyNonArchivedProductList();
        final ProductListDto dummyProductListDto = new ProductListDto(dummyProducts);
        final CartDto dummyCart = getCartWithOneDummyProduct();
        final SearchModel dummySearchModel = new SearchModel();
        BigDecimal priceLow = new BigDecimal("0");
        BigDecimal priceHigh = new BigDecimal("10000");
        dummySearchModel.setPriceLow(priceLow);
        dummySearchModel.setPriceHigh(priceHigh);
        dummySearchModel.setDescription(description);
        dummySearchModel.setSearchRequest(searchRequest);

        when(productService.findBySearchRequest(any())).thenReturn(dummyProductListDto);
        when(productService.filterByPrice(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(productService.searchForProductDescription(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(cartService.getCart()).thenReturn(dummyCart);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/description/?description=" + description));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", dummyProducts))
                .andExpect(model().attribute("cart", dummyCart))
                .andExpect(model().attribute("searchModel", dummySearchModel));

        verify(productService, times(1)).findBySearchRequest(any());
        verify(productService, times(1)).filterByPrice(any(), any());
        verify(productService, times(1)).searchForProductDescription(any(), any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/search/sortbyname", "/search/sortbyprice"})
    public void canSortProductsTest(String url) throws Exception {

        // given
        final String searchRequest = "product";
        final String description = "another";
        final List<Product> dummyProducts = getDummyNonArchivedProductList();
        final ProductListDto dummyProductListDto = new ProductListDto(dummyProducts);
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        final SearchModel dummySearchModel = new SearchModel();
        BigDecimal priceLow = BigDecimal.ZERO;
        BigDecimal priceHigh = BigDecimal.TEN;
        dummySearchModel.setDescription(description);
        dummySearchModel.setPriceLow(priceLow);
        dummySearchModel.setPriceHigh(priceHigh);
        dummySearchModel.setSearchRequest(searchRequest);

        when(productService.findBySearchRequest(any())).thenReturn(dummyProductListDto);
        when(productService.sortListByName(any(), any())).thenReturn(dummyProductListDto);
        when(productService.sortListByPrice(any(), any())).thenReturn(dummyProductListDto);
        when(productService.filterByPrice(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(productService.searchForProductDescription(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(cartService.getCart()).thenReturn(dummyCart);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);

        // when
        final ResultActions getResult = mockMvc.perform(get(url));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", dummyProducts))
                .andExpect(model().attribute("cart", dummyCart))
                .andExpect(model().attribute("searchModel", dummySearchModel));

        verify(productService, times(1)).findBySearchRequest(any());
        verify(productService, times(1)).filterByPrice(any(), any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/search/archived/sortbyname", "/search/archived/sortbyprice"})
    public void canSortArchivedProductsTest(String url) throws Exception {

        // given
        final List<Product> dummyProducts = getDummyArchivedProductList();
        final ProductListDto dummyProductListDto = new ProductListDto(dummyProducts);
        final CartDto dummyCart = getCartWithMultipleArchivedProducts();
        final SearchModel dummySearchModel = new SearchModel();
        dummySearchModel.setArchivedView(true);

        when(productService.findAllArchived()).thenReturn(dummyProductListDto);
        when(productService.sortListByName(any(), any())).thenReturn(dummyProductListDto);
        when(productService.sortListByPrice(any(), any())).thenReturn(dummyProductListDto);
        when(cartService.getCart()).thenReturn(dummyCart);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);

        // when
        final ResultActions getResult = mockMvc.perform(get(url));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", dummyProducts))
                .andExpect(model().attribute("cart", dummyCart))
                .andExpect(model().attribute("searchModel", dummySearchModel));

        verify(productService, times(1)).findAllArchived();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/search/pricelow/?priceLow=", "/search/pricehigh/?priceHigh="})
    void canFilterProductsByPriceTest(String url) throws Exception {

        // given
        final String searchRequest = "product";
        final String description = "another";
        final List<Product> dummyProducts = getDummyNonArchivedProductList();
        final ProductListDto dummyProductListDto = new ProductListDto(dummyProducts);
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        final SearchModel dummySearchModel = new SearchModel();
        BigDecimal priceLow = BigDecimal.ZERO;
        BigDecimal priceHigh = BigDecimal.TEN;
        dummySearchModel.setDescription(description);
        dummySearchModel.setPriceLow(priceLow);
        dummySearchModel.setPriceHigh(priceHigh);
        dummySearchModel.setSearchRequest(searchRequest);

        when(productService.findBySearchRequest(any())).thenReturn(dummyProductListDto);
        when(productService.filterByPrice(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(productService.searchForProductDescription(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(cartService.getCart()).thenReturn(dummyCart);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);

        // when
        final ResultActions getResult = mockMvc.perform(get(url + priceLow.toString()));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", dummyProducts))
                .andExpect(model().attribute("cart", dummyCart))
                .andExpect(model().attribute("searchModel", dummySearchModel));

        verify(productService, times(1)).findBySearchRequest(any());
        verify(productService, times(1)).filterByPrice(any(), any());
    }
}
