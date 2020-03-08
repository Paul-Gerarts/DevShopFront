package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.SaveStatus;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import be.syntra.devshop.DevshopFront.services.ProductService;
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
        //given
        Mockito.when(productService.createEmptyProduct()).thenReturn(new ProductDto());

        //when
        mockMvc.perform(get("/devshop/admin/addproduct"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addProduct"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Add Product")))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", new ProductDto()));

        //then
        verify(productService, times(1)).createEmptyProduct();
    }

    @Test
    void getProductEntry() throws Exception {
        //given
        ProductDto dummyProductDto = ProductDto.builder()
                .name("name")
                .price(new BigDecimal("55"))
                .build();
        Mockito.when(productService.addProduct(dummyProductDto)).thenReturn(SaveStatus.SAVED);

        //when
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

        //then
        verify(productService, times(1)).addProduct(dummyProductDto);
        verify(productService, times(0)).createEmptyProduct();
    }
}