package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.TestUtils.ProductUtils;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import be.syntra.devshop.DevshopFront.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void displayProductOverViewTest() throws Exception {

        // given
        final List<Product> dummyProductList = ProductUtils.getDummyProductList();
        Mockito.when(productService.findAll()).thenReturn(new ProductList(dummyProductList));

        // when
        final ResultActions getResult = mockMvc.perform(get("/devshop/products"));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", dummyProductList));
    }

    @Test
    void addSelectedProductToCart() throws Exception {
        // given
        final Product dummyProduct = ProductUtils.getDummyProduct();

        // when
        final ResultActions getResult = mockMvc.perform(post("/devshop/products/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("id", dummyProduct.getId().toString())
                .param("name", dummyProduct.getName())
                .param("price", dummyProduct.getPrice().toString()));

        // then
        getResult
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/devshop/products"));

        verify(productService, times(1)).addToCart(any(Product.class));
    }
}
