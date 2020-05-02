package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static be.syntra.devshop.DevshopFront.models.StatusNotification.*;

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
        restTemplate.delete(resourceUrl + "/categories/" + id);
        ResponseEntity<CategoryDto> categoryResponseEntity = restTemplate.getForEntity(resourceUrl + "/categories/" + id, CategoryDto.class);
        if (HttpStatus.NOT_FOUND.equals(categoryResponseEntity.getStatusCode())) {
            log.info("findById() -> category successful removed from database");
            return DELETED;
        } else if (HttpStatus.OK.equals(categoryResponseEntity.getStatusCode())) {
            return DELETE_FAIL;
        }
        return PERSISTANCE_ERROR;
    }
}
