package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.TestUtils.JsonUtils;
import be.syntra.devshop.DevshopFront.TestUtils.TestWebConfig;
import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static be.syntra.devshop.DevshopFront.TestUtils.ProductUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ProductServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
class ProductServiceImplTest {

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${productsEndpoint}")
    private String endpoint;

    @Autowired
    ProductServiceImpl productService;

    @MockBean
    CartService service;

    @Autowired
    private DataStore dataStore;

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
    void addProductTest() {
        // given
        final ProductDto dummyProductDto = getDummyProductDto();
        final String productDtoAsJson = jsonUtils.asJsonString(dummyProductDto);
        final String expectedEndpoint = baseUrl + endpoint;

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productDtoAsJson));

        // when
        StatusNotification statusNotification = productService.addProduct(dummyProductDto);

        // then
        mockServer.verify();
        assertEquals(StatusNotification.SAVED, statusNotification);
    }

    @Test
    void findAllNonArchivedTest() {
        // given
        final List<Product> dummyProductList = getDummyNonArchivedProductList();
        final ProductList expectedProductList = new ProductList(dummyProductList);
        final String dummyProductListJsonString = jsonUtils.asJsonString(expectedProductList);
        final String expectedEndpoint = baseUrl + endpoint;
        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(dummyProductListJsonString, MediaType.APPLICATION_JSON));

        // when
        final ProductList receivedProductList = productService.findAllNonArchived();

        // then
        mockServer.verify();
        assertEquals(expectedProductList.getProducts().size(), receivedProductList.getProducts().size());
    }

    @Test
    void findAllArchivedTest() {
        // given
        final List<Product> dummyArchivedProductList = getDummyArchivedProductList();
        final ProductList expectedProductList = new ProductList(dummyArchivedProductList);
        final String dummyArchivedProductListJsonString = jsonUtils.asJsonString(expectedProductList);
        final String expectedEndpoint = baseUrl + endpoint + "/archived";
        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(dummyArchivedProductListJsonString, MediaType.APPLICATION_JSON));

        // when
        final ProductList receivedProductList = productService.findAllArchived();

        // then
        mockServer.verify();
        assertEquals(expectedProductList.getProducts().size(), receivedProductList.getProducts().size());
    }

    @Test
    void findByIdTest() {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();
        final String dummyProductJsonString = jsonUtils.asJsonString(dummyProduct);
        final String expectedEndPoint = baseUrl + endpoint + "/details/" + dummyProduct.getId();
        mockServer
                .expect(requestTo(expectedEndPoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(dummyProductJsonString, MediaType.APPLICATION_JSON));

        // when
        final Product receivedProduct = productService.findById(dummyProduct.getId());

        // then
        mockServer.verify();
        assertThat(dummyProduct.toString()).isEqualTo(receivedProduct.toString());
    }

    @Test
    void archiveProductTest() {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();
        final String productDtoAsJson = jsonUtils.asJsonString(dummyProduct);
        final String expectedEndpoint = baseUrl + endpoint + "/update";

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(productDtoAsJson));

        // when
        StatusNotification statusNotification = productService.archiveProduct(dummyProduct);

        // then
        mockServer.verify();
        assertEquals(StatusNotification.UPDATED, statusNotification);
    }

    @Test
    void findBySearchRequestTest() {
        // given
        final String searchRequest = "product";
        final List<Product> dummyProductList = List.of(getDummyNonArchivedProduct());
        final ProductList expectedProductList = new ProductList(dummyProductList);
        final String dummyProductListJsonString = jsonUtils.asJsonString(expectedProductList);
        final String expectedEndpoint = baseUrl + endpoint + "/search/" + searchRequest;
        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(dummyProductListJsonString, MediaType.APPLICATION_JSON));

        // when
        final ProductList receivedProductList = productService.findBySearchRequest(searchRequest);

        // then
        mockServer.verify();
        assertEquals(expectedProductList.getProducts().size(), receivedProductList.getProducts().size());
    }
}