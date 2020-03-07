package be.syntra.devshop.DevshopFront.controller;

import be.syntra.devshop.DevshopFront.model.SaveStatus;
import be.syntra.devshop.DevshopFront.model.dto.ProductDto;
import be.syntra.devshop.DevshopFront.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void displayAddProductsFrom() throws Exception {

        Mockito.when(productService.createEmptyProduct()).thenReturn(new ProductDto());

        mockMvc.perform(get("/devshop/admin/addproduct"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addProduct"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Add Product")))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", new ProductDto()));

        verify(productService, times(1)).createEmptyProduct();
    }

    @Test
    void getProductEntry() throws Exception {
        ProductDto emptyProductDto = new ProductDto();
        ProductDto dummyProductDto = ProductDto.builder().name("name").price(new BigDecimal("55")).build();
        Mockito.when(productService.addProduct(dummyProductDto)).thenReturn(SaveStatus.SAVED);

        mockMvc.perform(
                post("/devshop/admin/addproduct")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name", "name")
                        .param("price", "55"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addProduct"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Add Product")))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", dummyProductDto));

        verify(productService, times(1)).addProduct(dummyProductDto);
        verify(productService, times(0)).createEmptyProduct();
    }
}