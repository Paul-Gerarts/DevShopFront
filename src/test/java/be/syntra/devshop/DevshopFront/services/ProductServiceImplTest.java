package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Category;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.*;
import be.syntra.devshop.DevshopFront.services.utils.CategoryMapper;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import be.syntra.devshop.DevshopFront.testutils.JsonUtils;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
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

import static be.syntra.devshop.DevshopFront.testutils.CategoryUtils.createCategoryDto;
import static be.syntra.devshop.DevshopFront.testutils.CategoryUtils.createCategoryList;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.*;
import static be.syntra.devshop.DevshopFront.testutils.StarRatingUtils.createStarRatingSetDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ProductServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class, ProductMapper.class})
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
    ProductMapper productMapper;

    @MockBean
    CategoryMapper categoryMapper;

    @MockBean
    CartService service;

    @MockBean
    SearchService searchService;

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
        final ProductDto dummyProduct = getDummyProductDto();
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
    void findAllWithOnlyCategoryTest() {
        // given
        final CategoryDto category = createCategoryDto();
        final List<Product> dummyAllProductList = getDummyAllProductList();
        final ProductList expectedProductList = ProductList.builder().products(dummyAllProductList).build();
        final String dummyAllProductListJsonString = jsonUtils.asJsonString(expectedProductList);
        final String expectedEndpoint = baseUrl + endpoint + "/all/" + category.getId();
        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        withSuccess(dummyAllProductListJsonString, MediaType.APPLICATION_JSON));

        // when
        final ProductList receivedProductList = productService.findAllWithOnlyCategory(category.getId());

        // then
        mockServer.verify();
        assertEquals(expectedProductList.getProducts().size(), receivedProductList.getProducts().size());
    }

    @Test
    void canGetAllCategoriesTest() {
        // given
        final List<Category> categories = createCategoryList();
        final CategoryList categoryListDummy = new CategoryList(categories);
        final String categoriesAsJson = jsonUtils.asJsonString(categoryListDummy);
        final String expectedEndpoint = baseUrl + endpoint + "/categories";

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(categoriesAsJson));

        // when
        CategoryList result = productService.findAllCategories();

        // then
        mockServer.verify();
        assertThat(result.getCategories().size()).isEqualTo(categories.size());
    }

    @Test
    void canGetAllRatingsFromProductTest() {
        // given
        final StarRatingSet ratings = createStarRatingSetDto();
        final String ratingsAsJson = jsonUtils.asJsonString(ratings);
        final String expectedEndpoint = baseUrl + endpoint + "/ratings/" + 1L;

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(ratingsAsJson));

        // when
        StarRatingSet result = productService.getRatingsFromProduct(1L);

        // then
        mockServer.verify();
        assertThat(result.getRatings().size()).isEqualTo(ratings.getRatings().size());
    }
}