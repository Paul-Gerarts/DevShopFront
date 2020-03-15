package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.TestUtils.ProductUtils;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SaveStatus;
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
        final ProductDto dummyProductDto = ProductUtils.getDummyProductDto();
        final String productDtoAsJson = ProductUtils.returnObjectAsJsonString(dummyProductDto);
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
        SaveStatus saveStatus = productService.addProduct(dummyProductDto);

        // then
        mockServer.verify();
        assertEquals(saveStatus, SaveStatus.SAVED);
    }

    @Test
    void findAll() throws Exception {
        // given
        final List<Product> dummyProductList = ProductUtils.getDummyProductList();
        final ProductList expectedProductList = new ProductList(dummyProductList);
        final String dummyProductListJsonString = ProductUtils.returnObjectAsJsonString(dummyProductList);
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