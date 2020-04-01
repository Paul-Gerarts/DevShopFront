package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.TestUtils.ProductUtils;
import be.syntra.devshop.DevshopFront.configuration.RestTemplateHeaderModifierInterceptor;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplateHeaderModifierInterceptor interceptor;

    @MockBean
    private JWTTokenExceptionHandler handler;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserService userService;

    @Test
    void displayAddProductsFrom() throws Exception {

        //given
        when(productService.createEmptyProduct()).thenReturn(new ProductDto());
        restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                connection.setInstanceFollowRedirects(false);
            }
        });
        //when
        final ResultActions getResult = mockMvc.perform(get("http://localhost8081/admin/addproduct"));

        //then
        getResult
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

        // given
        ProductDto dummyProductDto = ProductUtils.getDummyProductDto();
        when(productService.addProduct(dummyProductDto)).thenReturn(StatusNotification.SAVED);

        // when
        final ResultActions postRestult = mockMvc.perform(
                post("/admin/addproduct")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name", "name")
                        .param("price", "55"));

        final ResultActions getResult = mockMvc.perform(
                get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // then
        postRestult
                .andExpect(status().isCreated())
                .andExpect(view().name("admin/product/addProduct"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Add Product")))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", dummyProductDto));

        verify(productService, times(1)).addProduct(dummyProductDto);
        verify(productService, times(0)).createEmptyProduct();
    }
}