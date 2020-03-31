package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static be.syntra.devshop.DevshopFront.TestUtils.JsonUtils.returnObjectAsJsonString;
import static be.syntra.devshop.DevshopFront.TestUtils.ProductUtils.getDummyProductDto;
import static be.syntra.devshop.DevshopFront.TestUtils.ProductUtils.getDummyProductList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProductServiceImpl productService;

    MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void createEmptyProduct() {
        // given
        ProductDto emptyProductDto = new ProductDto();

        // when
        final ProductDto resultProductDto = productService.createEmptyProduct();

        // then
        assertEquals(resultProductDto, emptyProductDto);
    }

    @Test
    void addProductTest() throws Exception {
        // given
        final ProductDto dummyProductDto = getDummyProductDto();
        final String productDtoAsJson = returnObjectAsJsonString(dummyProductDto);
        final String expectedEndpoint = "http://localhost:8080/products";

        mockServer.expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productDtoAsJson)
                );

        // when
        StatusNotification statusNotification = productService.addProduct(dummyProductDto);

        // then
        mockServer.verify();
        assertEquals(StatusNotification.SAVED, statusNotification);
    }

    @Test
    void findAllTest() throws Exception {
        // given
        final List<Product> dummyProductList = getDummyProductList();
        final ProductList expectedProductList = new ProductList(dummyProductList);
        final String dummyProductListJsonString = returnObjectAsJsonString(dummyProductList);
        final String expectedEndpoint = "http://localhost:8080/products";
        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(dummyProductListJsonString, MediaType.APPLICATION_JSON)
                );

        // when
        final ProductList receivedProductList = productService.findAll();

        // then
        mockServer.verify();
        assertEquals(expectedProductList.getProductList().size(), receivedProductList.getProductList().size());
    }
}