package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ReviewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static be.syntra.devshop.DevshopFront.models.StatusNotification.*;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${productsEndpoint}")
    private String endpoint;

    private String resourceUrl = null;
    private final static String REVIEW_ENDPOINT = "/reviews";


    @Autowired
    private RestTemplate restTemplate;

    private ProductService productService;

    @Autowired
    public ReviewServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @PostConstruct
    private void init() {
        resourceUrl = baseUrl.concat(endpoint);
    }

    @Override
    public StatusNotification submitReview(Long productId, Review review) {
        ReviewDto reviewDto = buildReviewDto(productId, review);
        ResponseEntity<ReviewDto> reviewDtoResponseEntity = restTemplate.postForEntity(resourceUrl + REVIEW_ENDPOINT, reviewDto, ReviewDto.class);
        if (HttpStatus.CREATED.equals(reviewDtoResponseEntity.getStatusCode())) {
            return REVIEW_ADDED;
        }
        return REVIEW_ADD_FAIL;
    }

    @Override
    public StatusNotification updateReview(Long productId, Review review) {
        ReviewDto reviewDto = buildReviewDto(productId, review);
        final ResponseEntity<ReviewDto> reviewDtoResponseEntity = restTemplate.exchange(resourceUrl + REVIEW_ENDPOINT, HttpMethod.PUT, new HttpEntity<>(reviewDto), ReviewDto.class);
        if (HttpStatus.OK.equals(reviewDtoResponseEntity.getStatusCode())) {
            return REVIEW_UPDATED;
        }
        return REVIEW_UPDATE_FAIL;
    }

    /*
     *  using .exchange() method otherwise with .delete() the body is not sent
     */

    @Override
    public StatusNotification removeReview(Long productId, Review review) {
        ReviewDto reviewDto = buildReviewDto(productId, review);
        final ResponseEntity<ReviewDto> reviewDtoResponseEntity = restTemplate.exchange(resourceUrl + REVIEW_ENDPOINT, HttpMethod.DELETE, new HttpEntity<>(reviewDto), ReviewDto.class);
        if (HttpStatus.OK.equals(reviewDtoResponseEntity.getStatusCode())) {
            return REVIEW_DELETED;
        }
        return REVIEW_DELETE_FAIL;
    }
    @Override
    public Review findByUserNameAndId(Long id, String userName) {
        return productService.findById(id).getReviews().stream()
                .filter(r -> r.getUserName().equals(userName))
                .findFirst()
                .orElse(
                        Review.builder()
                                .userName(userName)
                                .build());
    }

    private ReviewDto buildReviewDto(Long productId, Review review) {
        return ReviewDto.builder()
                .reviewText(review.getReviewText())
                .userName(review.getUserName())
                .productId(productId)
                .build();
    }
}

