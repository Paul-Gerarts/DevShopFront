package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ReviewDto;
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

import static be.syntra.devshop.DevshopFront.testutils.ReviewUtils.getDummyReview;
import static be.syntra.devshop.DevshopFront.testutils.ReviewUtils.getDummyReviewDto;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@RestClientTest(ReviewServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
public class ReviewServiceImplTest {
    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    RestTemplate restTemplate;

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${productsEndpoint}")
    private String endpoint;

    @Autowired
    ReviewServiceImpl reviewService;

    @MockBean
    CategoryMapper categoryMapper;

    @MockBean
    ProductMapper productMapper;

    @MockBean
    CartService service;

    @MockBean
    SearchService searchService;

    @MockBean
    ProductService productService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void canSubmitReviewTest() {
        // given
        final Long productId = 1L;
        final Review dummyReview = getDummyReview();
        final ReviewDto dummyReviewDto = getDummyReviewDto();
        final String reviewDtoAsJson = jsonUtils.asJsonString(dummyReviewDto);
        final String expectedEndpoint = baseUrl + endpoint + "/reviews";

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(reviewDtoAsJson)
                );

        // when
        StatusNotification statusNotification = reviewService.submitReview(productId, dummyReview);

        // then
        mockServer.verify();
        assertEquals(StatusNotification.SUCCESS, statusNotification);
    }

    @Test
    void canUpdateReview() {
        // given
        final Long productId = 1L;
        final Review dummyReview = getDummyReview();
        final ReviewDto dummyReviewDto = getDummyReviewDto();
        final String expectedEndpoint = baseUrl + endpoint + "/reviews";

        // when
        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.OK)
                );
        reviewService.updateReview(productId, dummyReview);

        // then
        mockServer.verify();
    }

    @Test
    void canDeleteReview() {
        // given
        final Long productId = 1L;
        final Review dummyReview = getDummyReview();
        final ReviewDto dummyReviewDto = getDummyReviewDto();
        final String reviewDtoAsJson = jsonUtils.asJsonString(dummyReviewDto);
        final String expectedEndpoint = baseUrl + endpoint + "/reviews";

        // when
        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(reviewDtoAsJson)
                );
        reviewService.removeReview(productId, dummyReview);

        // then
        mockServer.verify();
    }
}
