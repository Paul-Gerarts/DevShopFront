package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.TestUtils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.TestUtils.TestWebConfig;
import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import be.syntra.devshop.DevshopFront.models.dto.SearchDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.CartUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static be.syntra.devshop.DevshopFront.TestUtils.ProductUtils.getDummyNonArchivedProductList;
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

    // todo: (DEV-015) might change
    @MockBean
    private SearchService searchService;

    @Test
    public void canDisplaySearchedForProductsTest() throws Exception {
        // given
        final String searchRequest = "product";
        final List<Product> dummyProducts = getDummyNonArchivedProductList();
        final ProductList dummyProductList = new ProductList(dummyProducts);
        final CartDto dummyCart = CartUtils.getCartWithOneDummyProduct();
        final SearchDto dummySearchDto = new SearchDto();
        dummySearchDto.setBasicSearchTerm(searchRequest);

        when(productService.findBySearchRequest(any())).thenReturn(dummyProductList);
        when(cartService.getCart()).thenReturn(dummyCart);
        // todo: (DEV-015) might change
        when(searchService.getSearchDto()).thenReturn(dummySearchDto);
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

        verify(productService, times(1)).findBySearchRequest(searchRequest);
    }
}
