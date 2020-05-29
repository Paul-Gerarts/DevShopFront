package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static be.syntra.devshop.DevshopFront.models.StatusNotification.*;

@Service
@Slf4j
public class StarRatingServiceImpl implements StarRatingService {

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
    public StarRatingDto findByUserNameAndId(Long productId, String userName) {
        ResponseEntity<StarRatingDto> ratingResponseEntity = restTemplate.getForEntity(resourceUrl + "/" + userName + "/ratings/" + productId, StarRatingDto.class);
        if (HttpStatus.OK.equals(ratingResponseEntity.getStatusCode())) {
            return ratingResponseEntity.getBody();
        }
        return new StarRatingDto();
    }

    @Override
    public StatusNotification submitRating(Long productId, Double count, String userName) {
        if (StringUtils.isBlank(userName)) {
            return NOT_AUTHORIZED;
        }
        StarRatingDto starRatingDto = StarRatingDto.builder()
                .productId(productId)
                .rating(count)
                .userName(userName)
                .build();
        ResponseEntity<StarRatingDto> starRatingDtoResponseEntity = restTemplate.postForEntity(resourceUrl + "/ratings", starRatingDto, StarRatingDto.class);
        if (HttpStatus.CREATED.equals(starRatingDtoResponseEntity.getStatusCode())) {
            log.info("submitRating() -> saved {} ", starRatingDto);
            return SUCCESS;
        }
        return RATING_FAIL;
    }
}
