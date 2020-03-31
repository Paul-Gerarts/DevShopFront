package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.TestUtils.ProductUtils;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(JWTTokenExceptionHandler.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private JWTTokenExceptionHandler jwtTokenExceptionHandler;

    @MockBean
    private UserService userService;

    @Test
    public void displayProductOverViewTest() throws Exception {

        // given
        final List<Product> dummyProductList = ProductUtils.getDummyProductList();
        Mockito.when(productService.findAll()).thenReturn(new ProductList(dummyProductList));

        // when
        final ResultActions getResult = mockMvc.perform(get("/products"));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", dummyProductList));
    }
}
