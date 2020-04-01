package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.factories.RestTemplateFactory;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
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

    @Mock
    RestTemplate restTemplate;

    @Autowired
    private RestTemplateFactory restTemplateFactory = new RestTemplateFactory();

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${productsEndpoint}")
    private String endpoint;

    @Autowired
    ProductServiceImpl productService;

    MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        restTemplate = restTemplateFactory.ofSecurity();
        mockServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
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
        final String expectedEndpoint = baseUrl + endpoint;

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productDtoAsJson)
                );

        // when
        ResponseEntity result = restTemplate.postForEntity(expectedEndpoint, new HttpEntity<ProductDto>(dummyProductDto), ProductDto.class);
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
        final String expectedEndpoint = baseUrl + endpoint;
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