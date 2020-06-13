package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dtos.ProductsDisplayListDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import be.syntra.devshop.DevshopFront.testutils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static be.syntra.devshop.DevshopFront.testutils.CartUtils.getCartProductsDisplayDto;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaginationController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
@ExtendWith(MockitoExtension.class)
public class PaginationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private CartService cartService;

    @MockBean
    private SearchService searchService;

    @InjectMocks
    private PaginationController paginationController;

    @ParameterizedTest
    @ValueSource(strings = {"/pagination/previous", "/pagination/next", "/pagination/first", "/pagination/last/5"})
    void canRequestFirstPreviousNextPages(String url) throws Exception {
        // given
        SearchModel searchModel = new SearchModel();
        searchModel.setPageSize(5);
        final ProductsDisplayListDto productsDisplayListDto = new ProductsDisplayListDto();
        when(searchService.getSearchModel()).thenReturn(searchModel);
        when(productMapper.convertToProductsDisplayListDto(any())).thenReturn(productsDisplayListDto);
        when(cartService.getCartDisplayDto()).thenReturn(getCartProductsDisplayDto());
        when(productService.findAllProductsBySearchModel()).thenReturn(getDummyProductList());
        doNothing().when(searchService).requestNextPage();
        doNothing().when(searchService).requestPreviousPage();
        doNothing().when(searchService).requestFirstPage();
        doNothing().when(searchService).requestLastPage(5);

        // when
        final ResultActions getResult = mockMvc.perform(get(url));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists(
                        "pageSizeList",
                        "selectedPageSize",
                        "searchModel",
                        "productlist",
                        "cart"));

        verify(searchService, times(2)).getSearchModel();
        verify(productMapper, times(1)).convertToProductsDisplayListDto(any());
        verify(cartService, times(1)).getCartDisplayDto();
        verify(productService, times(1)).findAllProductsBySearchModel();
    }

    @Test
    void canChangePageSize() throws Exception {
        // given
        SearchModel searchModel = new SearchModel();
        searchModel.setPageSize(5);
        final ProductsDisplayListDto productsDisplayListDto = new ProductsDisplayListDto();
        when(searchService.getSearchModel()).thenReturn(searchModel);
        when(productMapper.convertToProductsDisplayListDto(any())).thenReturn(productsDisplayListDto);
        when(cartService.getCartDisplayDto()).thenReturn(getCartProductsDisplayDto());
        when(productService.findAllProductsBySearchModel()).thenReturn(getDummyProductList());

        // when
        final ResultActions getResult = mockMvc.perform(post("/pagination/size")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("pageSize", "10"));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists(
                        "pageSizeList",
                        "selectedPageSize",
                        "searchModel",
                        "productlist",
                        "cart"))
                .andExpect(model().attribute("selectedPageSize", 10));


        verify(searchService, times(4)).getSearchModel();
        verify(productMapper, times(1)).convertToProductsDisplayListDto(any());
        verify(cartService, times(1)).getCartDisplayDto();
        verify(productService, times(1)).findAllProductsBySearchModel();
    }
}
