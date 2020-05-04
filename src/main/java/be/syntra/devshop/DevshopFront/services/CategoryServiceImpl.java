package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryChangeDto;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

import static be.syntra.devshop.DevshopFront.models.StatusNotification.*;
import static java.util.Objects.requireNonNull;

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
        if (StringUtils.isEmpty(requireNonNull(categoryResponseEntity.getBody()).getName())) {
            log.info("findById() -> category successful removed from database");
            return DELETED;
        } else {
            return DELETE_FAIL;
        }
    }

    @Override
    public StatusNotification setNewCategories(Long categoryToDelete, Long categoryToSet) {
        CategoryChangeDto categoryChangeDto = CategoryChangeDto.builder()
                .categoryToDelete(categoryToDelete)
                .categoryToSet(categoryToSet)
                .build();
        ResponseEntity<CategoryChangeDto> categoryResponseEntity = restTemplate.postForEntity(resourceUrl + "/categories/set_category", categoryChangeDto, CategoryChangeDto.class);
        if (HttpStatus.OK.equals(categoryResponseEntity.getStatusCode())) {
            log.info("setNewCategories() -> products succesfully switched category");
            return SUCCESS;
        }
        return PERSISTENCE_ERROR;
    }
}
