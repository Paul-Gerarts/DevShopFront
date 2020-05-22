package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyNonArchivedProduct;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.getDummyProductDto;
import static be.syntra.devshop.DevshopFront.testutils.StarRatingUtils.createStarRatingDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(StarRatingServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StarRatingServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private StarRatingService ratingService;

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
    void canGetRatingFromUserTest() {
        // given
        final StarRatingDto ratingDto = createStarRatingDto();
        final Product product = getDummyNonArchivedProduct();
        final String dummyRatingDtoJsonString = jsonUtils.asJsonString(ratingDto);
        final String expectedEndpoint = baseUrl + endpoint + "/" + ratingDto.getUserName() + "/ratings/" + product.getId();

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(dummyRatingDtoJsonString, MediaType.APPLICATION_JSON));

        // when
        StarRatingDto result = ratingService.findByUserNameAndId(product.getId(), ratingDto.getUserName());

        // then
        mockServer.verify();
        assertEquals(result.toString(), ratingDto.toString());
    }

    @Test
    void submitRatingTest() {
        // given
        final StarRatingDto starRatingDto = createStarRatingDto();
        final ProductDto dummyProductDto = getDummyProductDto();
        final String starRatingDtoAsJson = jsonUtils.asJsonString(starRatingDto);
        final String expectedEndpoint = baseUrl + endpoint + "/ratings";

        mockServer
                .expect(requestTo(expectedEndpoint))
                .andExpect(method(HttpMethod.POST))
                .andRespond(
                        MockRestResponseCreators
                                .withStatus(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(starRatingDtoAsJson));

        // when
        StatusNotification statusNotification = ratingService.submitRating(dummyProductDto.getId(), starRatingDto.getRating(), starRatingDto.getUserName());

        // then
        mockServer.verify();
        assertEquals(StatusNotification.SUCCESS, statusNotification);
    }
}
