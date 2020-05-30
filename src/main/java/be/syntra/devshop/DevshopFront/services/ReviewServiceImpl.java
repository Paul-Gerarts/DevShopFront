package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ReviewDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static be.syntra.devshop.DevshopFront.models.StatusNotification.REVIEW_FAIL;
import static be.syntra.devshop.DevshopFront.models.StatusNotification.SUCCESS;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${productsEndpoint}")
    private String endpoint;

    private String resourceUrl = null;

    @Autowired
    private RestTemplate restTemplate;

    @PostConstruct
    private void init() {
        resourceUrl = baseUrl.concat(endpoint);
    }

    @Override
    public StatusNotification submitReview(Long productId, Review review) {
        ReviewDto reviewDto = ReviewDto.builder()
                .reviewText(review.getReviewText())
                .userName(review.getUserName())
                .productId(productId)
                .build();
        ResponseEntity<ReviewDto> reviewDtoResponseEntity = restTemplate.postForEntity(resourceUrl + "/reviews", reviewDto, ReviewDto.class);
        if (HttpStatus.CREATED.equals(reviewDtoResponseEntity.getStatusCode())) {
            log.info("submitReview() -> saved {} ", reviewDto);
            return SUCCESS;
        }
        return REVIEW_FAIL;
    }

    @Override
    public void updateReview(Long productId, Review review) {
        ReviewDto reviewDto = ReviewDto.builder()
                .reviewText(review.getReviewText())
                .userName(review.getUserName())
                .productId(productId)
                .build();
        restTemplate.put(resourceUrl + "/reviews", reviewDto);
    }

    @Override
    public void removeReview(Long productId, Review review) {
        ReviewDto reviewDto = ReviewDto.builder()
                .reviewText(review.getReviewText())
                .userName(review.getUserName())
                .productId(productId)
                .build();
        restTemplate.delete(resourceUrl + "/reviews", reviewDto);
    }
}

