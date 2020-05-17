package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

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
    public StarRatingDto findBy(Long productId, String userName) {
        ResponseEntity<StarRatingDto> ratingResponseEntity = restTemplate.getForEntity(resourceUrl + "/" + userName + "/ratings/" + productId, StarRatingDto.class);
        if (HttpStatus.OK.equals(ratingResponseEntity.getStatusCode())) {
            log.info("findBy() -> rating retrieved from backEnd");
            return ratingResponseEntity.getBody();
        }
        return new StarRatingDto();
    }
}
