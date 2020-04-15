package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.TestUtils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.TestUtils.TestWebConfig;
import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.CartDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductListCacheService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.utils.CartUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static be.syntra.devshop.DevshopFront.TestUtils.ProductUtils.getDummyNonArchivedProduct;
import static be.syntra.devshop.DevshopFront.TestUtils.ProductUtils.getDummyNonArchivedProductList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CartService cartService;

    @MockBean
    private ProductListCacheService productListCacheService;

    @Test
    public void displayProductOverViewTest() throws Exception {

        // given
        final List<Product> dummyProducts = getDummyNonArchivedProductList();
        final CartDto dummyCartDto = CartUtils.getCartWithOneDummyProduct();
        final ProductListCache productListCache = new ProductListCache();
        productListCache.setCachedProductList(dummyProducts);
        when(cartService.getCart()).thenReturn(dummyCartDto);
        when(productListCacheService.getProductListCache()).thenReturn(productListCache);

        // when
        final ResultActions getResult = mockMvc.perform(get("/products"));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", dummyProducts))
                .andExpect(model().attribute("cart", dummyCartDto));


        verify(productListCacheService, times(1)).getProductListCache();
    }

    @Test
    void displayProductDetailsTest() throws Exception {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();
        //when(productService.findById(dummyProduct.getId())).thenReturn(dummyProduct);
        when(productListCacheService.findById(dummyProduct.getId())).thenReturn(dummyProduct);

        // when
        final ResultActions getResult = mockMvc.perform(get("/products/details/" + dummyProduct.getId()));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productDetails"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", dummyProduct));

        //verify(productService, times(1)).findById(dummyProduct.getId());
        verify(productListCacheService, times(1)).findById(dummyProduct.getId());
    }

    @Test
    void canArchiveProductTest() throws Exception {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();
        when(productService.findById(dummyProduct.getId())).thenReturn(dummyProduct);
        when(productService.archiveProduct(dummyProduct)).thenReturn(StatusNotification.UPDATED);

        // when
        final ResultActions getResult = mockMvc.perform(post("/products/details/" + dummyProduct.getId()));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productDetails"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("product", "status"))
                .andExpect(model().attribute("product", dummyProduct))
                .andExpect(model().attribute("status", StatusNotification.UPDATED));

        verify(productService, times(1)).findById(dummyProduct.getId());
        verify(productService, times(1)).archiveProduct(dummyProduct);
    }

    @Test
    void addSelectedProductToCart() throws Exception {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();

        // when
        final ResultActions getResult = mockMvc.perform(post("/products/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("id", dummyProduct.getId().toString())
                .param("name", dummyProduct.getName())
                .param("price", dummyProduct.getPrice().toString()));

        // then
        getResult
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products"));

        verify(productService, times(1)).addToCart(any());
        verify(productListCacheService, times(1)).findById(dummyProduct.getId());
    }
}
