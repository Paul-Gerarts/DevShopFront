package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SaveStatus;
import be.syntra.devshop.DevshopFront.models.dto.LogInDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

public class AuthorisationServiceImpl implements AuthorisationService {

    @Value("${baseUrl}")
    String baseUrl;
    @Value("${loginEndpoint}")
    String endpoint;

    Logger logger = LoggerFactory.getLogger(AuthorisationServiceImpl.class);

    @PostMapping("/login")
    public SaveStatus login(@RequestBody LogInDto logInDto) {
        final String url = baseUrl.concat(endpoint);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<LogInDto> request = new HttpEntity<>(logInDto, httpHeaders);
        try {
            ResponseEntity<LogInDto> productDtoResponseEntity = restTemplate.postForEntity(url, request, LogInDto.class);
            if (HttpStatus.CREATED.equals(productDtoResponseEntity.getStatusCode())) {
                logger.info("addProduct() -> saved > " + logInDto.toString());
                return SaveStatus.SAVED;
            }
        } catch (Exception e) {
            logger.error("addProduct() -> " + e.getCause().toString());
            logger.error("addProduct() -> " + e.getLocalizedMessage());
        }
        return SaveStatus.ERROR;
    }
}
