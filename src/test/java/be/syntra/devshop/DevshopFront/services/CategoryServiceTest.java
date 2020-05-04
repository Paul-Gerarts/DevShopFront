package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.testutils.JsonUtils;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.CategoryUtils.createCategoryDto;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyAllProductList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(CategoryServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${productsEndpoint}")
    private String endpoint;

    MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void canDeleteCategoryTest() {
        // given
        final CategoryDto categoryDto = CategoryDto.builder().build();
        final String categoryDtoAsJson = jsonUtils.asJsonString(categoryDto);
        final String expectedEndpoint = baseUrl + endpoint + "/categories/" + categoryDto.getId();

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withNoContent());

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(categoryDtoAsJson, MediaType.APPLICATION_JSON));

        // when
        StatusNotification statusNotification = categoryService.delete(categoryDto.getId());

        // then
        mockServer.verify();
        assertEquals(StatusNotification.DELETED, statusNotification);
    }

    @Test
    void canSetNewCategoryTest() {
        // given
        final CategoryDto categoryDto = createCategoryDto();
        final long categoryToSet = 2L;
        final List<Product> dummyAllProductList = getDummyAllProductList();
        final ProductList expectedProductList = new ProductList(dummyAllProductList);
        final String dummyAllProductListJsonString = jsonUtils.asJsonString(expectedProductList);
        final String expectedEndpoint = baseUrl + endpoint + "/categories/set_category";

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(dummyAllProductListJsonString, MediaType.APPLICATION_JSON));

        // when
        StatusNotification statusNotification = categoryService.setNewCategories(categoryDto.getId(), categoryToSet);

        // then
        mockServer.verify();
        assertEquals(StatusNotification.SUCCESS, statusNotification);
    }
}
