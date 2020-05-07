package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductListDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapperUtil;
import be.syntra.devshop.DevshopFront.testutils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.Test;
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
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyNonArchivedProductList;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductDtoList;
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

    @MockBean
    private CartService cartService;

    @MockBean
    private SearchService searchService;

    @MockBean
    private ProductMapperUtil productMapperUtil;

    @Test
    void showSearchBarResultTest() throws Exception {
        // given
        final String TEST_REQUEST = "testRequest";
        final ProductListDto dummyProductListDto = new ProductListDto(getDummyNonArchivedProductList());
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListDto);
        when(productMapperUtil.convertToProductDtoList(any())).thenReturn(getDummyProductDtoList());
        when(searchService.getSearchModel()).thenReturn(new SearchModel());
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/?searchRequest=" + TEST_REQUEST));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("searchModel", "productlist", "cart"));

        verify(searchService, times(1)).setSearchRequest(TEST_REQUEST);
        verify(searchService, times(1)).setSearchResultView(true);
        verify(searchService, times(1)).setArchivedView(false);
        verify(productService, times(1)).findAllProductsBySearchModel();
    }

    @Test
    void searchPriceLowTest() throws Exception {
        //given
        final String PRICE_LOW = "6.66";
        final BigDecimal price_low_big_d = new BigDecimal(PRICE_LOW);
        final ProductListDto dummyProductListDto = new ProductListDto(getDummyNonArchivedProductList());
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListDto);
        when(productMapperUtil.convertToProductDtoList(any())).thenReturn(getDummyProductDtoList());
        when(searchService.getSearchModel()).thenReturn(new SearchModel());
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/pricelow/?priceLow=" + PRICE_LOW));

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
        final String PRICE_HIGH = "9999.99";
        final BigDecimal price_low_big_d = new BigDecimal(PRICE_HIGH);
        final ProductListDto dummyProductListDto = new ProductListDto(getDummyNonArchivedProductList());
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListDto);
        when(productMapperUtil.convertToProductDtoList(any())).thenReturn(getDummyProductDtoList());
        when(searchService.getSearchModel()).thenReturn(new SearchModel());
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/pricehigh/?priceHigh=" + PRICE_HIGH));

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
        final String DESCRIPTION = "my prod description";
        final ProductListDto dummyProductListDto = new ProductListDto(getDummyNonArchivedProductList());
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListDto);
        when(productMapperUtil.convertToProductDtoList(any())).thenReturn(getDummyProductDtoList());
        when(searchService.getSearchModel()).thenReturn(new SearchModel());
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/description/?description=" + DESCRIPTION));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("searchModel", "productlist", "cart"));

        verify(searchService, times(1)).setDescription(DESCRIPTION);
        verify(searchService, times(1)).setSearchResultView(true);
        verify(searchService, times(1)).setArchivedView(false);
        verify(productService, times(1)).findAllProductsBySearchModel();
    }

    @Test
    void sortByNameTest() throws Exception {
        // given
        final ProductListDto dummyProductListDto = new ProductListDto(getDummyNonArchivedProductList());
        final SearchModel searchModel = SearchModel.builder().searchRequest("").searchResultView(true).build();
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();

        when(searchService.getSearchModel()).thenReturn(searchModel);
        //when(productService.sortListByName(anyList(), any(SearchModel.class))).thenReturn(dummyProductListDto);
        when(searchService.getSearchModel()).thenReturn(searchModel);
        when(productMapperUtil.convertToProductDtoList(any())).thenReturn(getDummyProductDtoList());
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListDto);
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/sortbyname"));

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

    @Test
    void sortByPriceTest() throws Exception {
        // given
        final ProductListDto dummyProductListDto = new ProductListDto(getDummyNonArchivedProductList());
        final SearchModel searchModel = SearchModel.builder().searchRequest("").searchResultView(true).build();
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();

        when(searchService.getSearchModel()).thenReturn(searchModel);
        //when(productService.sortListByPrice(anyList(), any(SearchModel.class))).thenReturn(dummyProductListDto);
        when(searchService.getSearchModel()).thenReturn(searchModel);
        when(productMapperUtil.convertToProductDtoList(any())).thenReturn(getDummyProductDtoList());
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListDto);
        when(cartService.getCart()).thenReturn(dummyCart);

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/sortbyprice"));

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
}
