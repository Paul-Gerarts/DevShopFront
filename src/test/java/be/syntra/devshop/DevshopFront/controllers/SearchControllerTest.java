package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
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

import static be.syntra.devshop.DevshopFront.testutils.CartUtils.getCartWithMultipleNonArchivedProducts;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductDtoList;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SearchController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
class SearchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CartService cartService;

    @MockBean
    private SearchService searchService;

    @MockBean
    private ProductMapper productMapper;

    @Test
    void showSearchBarResultTest() throws Exception {
        // given
        final String testRequest = "testRequest";
        final ProductList dummyProductList = getDummyProductList();
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductList);
        when(productMapper.convertToProductsDisplayListDto(any())).thenReturn(getDummyProductDtoList());
        when(searchService.getSearchModel()).thenReturn(new SearchModel());
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/?searchRequest=" + testRequest));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("searchModel", "productlist", "cart"));

        verify(searchService, times(1)).setSearchRequest(testRequest);
        verify(searchService, times(1)).setSearchResultView(true);
        verify(searchService, times(1)).setArchivedView(false);
        verify(productService, times(1)).findAllProductsBySearchModel();
    }

    @Test
    void searchPriceLowTest() throws Exception {
        //given
        final String priceLow = "6.66";
        final BigDecimal price_low_big_d = new BigDecimal(priceLow);
        final ProductList dummyProductList = getDummyProductList();
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductList);
        when(productMapper.convertToProductsDisplayListDto(any())).thenReturn(getDummyProductDtoList());
        when(searchService.getSearchModel()).thenReturn(new SearchModel());
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/pricelow/?priceLow=" + priceLow));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("searchModel", "productlist", "cart"));

        verify(searchService, times(1)).setPriceLow(price_low_big_d);
        verify(searchService, times(1)).setSearchResultView(true);
        verify(searchService, times(1)).setArchivedView(false);
        verify(productService, times(1)).findAllProductsBySearchModel();
    }

    @Test
    void searchPriceHighTest() throws Exception {
        //given
        final String priceHigh = "9999.99";
        final BigDecimal price_low_big_d = new BigDecimal(priceHigh);
        final ProductList dummyProductList = getDummyProductList();
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductList);
        when(productMapper.convertToProductsDisplayListDto(any())).thenReturn(getDummyProductDtoList());
        when(searchService.getSearchModel()).thenReturn(new SearchModel());
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/pricehigh/?priceHigh=" + priceHigh));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("searchModel", "productlist", "cart"));

        verify(searchService, times(1)).setPriceHigh(price_low_big_d);
        verify(searchService, times(1)).setSearchResultView(true);
        verify(searchService, times(1)).setArchivedView(false);
        verify(productService, times(1)).findAllProductsBySearchModel();
    }

    @Test
    void searchProductDescriptionTest() throws Exception {
        //given
        final String description = "my prod description";
        final ProductList dummyProductList = getDummyProductList();
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductList);
        when(productMapper.convertToProductsDisplayListDto(any())).thenReturn(getDummyProductDtoList());
        when(searchService.getSearchModel()).thenReturn(new SearchModel());
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/description/?description=" + description));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("searchModel", "productlist", "cart"));

        verify(searchService, times(1)).setDescription(description);
        verify(searchService, times(1)).setSearchResultView(true);
        verify(searchService, times(1)).setArchivedView(false);
        verify(productService, times(1)).findAllProductsBySearchModel();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/search/sortbyname", "/search/sortbyprice"})
    void canSortProductsTest(String url) throws Exception {
        // given
        final ProductList dummyProductList = getDummyProductList();
        final SearchModel searchModel = SearchModel.builder().searchRequest("").searchResultView(true).build();
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();

        when(searchService.getSearchModel()).thenReturn(searchModel);
        when(productMapper.convertToProductsDisplayListDto(any())).thenReturn(getDummyProductDtoList());
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductList);
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get(url));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("searchModel", "productlist", "cart"));

        verify(searchService, times(1)).setSearchResultView(true);
        verify(searchService, times(1)).setArchivedView(false);
        verify(productService, times(1)).findAllProductsBySearchModel();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/search/archived/sortbyname", "/search/archived/sortbyprice"})
    void canSortArchivedProductsTest(String url) throws Exception {
        // given
        final ProductList dummyProductList = getDummyProductList();
        final SearchModel searchModel = SearchModel.builder().searchRequest("").searchResultView(true).build();
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();

        when(searchService.getSearchModel()).thenReturn(searchModel);
        when(productMapper.convertToProductsDisplayListDto(any())).thenReturn(getDummyProductDtoList());
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductList);
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get(url));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("searchModel", "productlist", "cart"));

        verify(searchService, times(1)).setArchivedView(true);
        verify(productService, times(1)).findAllProductsBySearchModel();
    }

}
