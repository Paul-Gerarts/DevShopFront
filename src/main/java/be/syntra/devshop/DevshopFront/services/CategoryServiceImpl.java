package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.CategoryNotFoundException;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static be.syntra.devshop.DevshopFront.models.StatusNotification.DELETED;
import static be.syntra.devshop.DevshopFront.models.StatusNotification.PERSISTANCE_ERROR;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

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
    public StatusNotification delete(Long id) {
        ResponseEntity<Long> categoryResponseEntity = restTemplate.postForEntity(resourceUrl + "/categories/", id, Long.class);
        if (HttpStatus.OK.equals(categoryResponseEntity.getStatusCode())) {
            log.info("findById() -> category successful removed from database");
            return DELETED;
        } else if (HttpStatus.NOT_FOUND.equals(categoryResponseEntity.getStatusCode())) {
            throw new CategoryNotFoundException("Category with id = " + id + " was not found");
        }
        return PERSISTANCE_ERROR;
    }
}
