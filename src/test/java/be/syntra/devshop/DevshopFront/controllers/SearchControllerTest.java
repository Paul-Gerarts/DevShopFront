package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.Product;
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
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.*;
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

        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListDto);
        when(productMapperUtil.convertToProductDtoList(any(ProductListDto.class))).thenReturn(getDummyProductDtoList());

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/?searchRequest=" + searchRequest));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("productlist", "cart"));

        verify(productMapperUtil, times(1)).convertToProductDtoList(any());
        verify(searchService, times(1)).getSearchModel();
        verify(cartService, times(1)).getCart();
        verify(searchService, times(1)).setSearchRequest(any());
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

        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListDto);
        when(productService.filterByPrice(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(productService.searchForProductDescription(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(cartService.getCart()).thenReturn(dummyCart);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);
        when(productMapperUtil.convertToProductDtoList(any(ProductListDto.class))).thenReturn(getDummyProductDtoList());

        // when
        final ResultActions getResult = mockMvc.perform(get("/search/description/?description=" + description));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("productlist", "cart", "searchModel"));

        verify(productMapperUtil, times(1)).convertToProductDtoList(any());
        verify(searchService, times(1)).getSearchModel();
        verify(cartService, times(1)).getCart();
        verify(searchService, times(1)).setDescription(any());
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
        dummySearchModel.setActiveFilters(true);

        when(productService.findBySearchRequest(any())).thenReturn(dummyProductListDto);
        when(productService.sortListByName(any(), any())).thenReturn(dummyProductListDto);
        when(productService.sortListByPrice(any(), any())).thenReturn(dummyProductListDto);
        when(productService.filterByPrice(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(productService.searchForProductDescription(dummyProducts, dummySearchModel)).thenReturn(dummyProductListDto);
        when(cartService.getCart()).thenReturn(dummyCart);
        when(searchService.getSearchModel()).thenReturn(dummySearchModel);
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListDto);

        // when
        final ResultActions getResult = mockMvc.perform(get(url));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("productlist", "cart", "searchModel"));

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

    /*

     */
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
        final SearchModel searchModel = SearchModel.builder().searchResultView(true).build();
        final CartDto dummyCart = getCartWithMultipleNonArchivedProducts();

        when(searchService.getSearchModel()).thenReturn(searchModel);
        when(productService.sortListByName(anyList(), any(SearchModel.class))).thenReturn(dummyProductListDto);
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
}
