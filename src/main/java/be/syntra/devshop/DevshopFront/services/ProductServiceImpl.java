package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

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
    public ProductDto createEmptyProduct() {
        return new ProductDto();
    }

    @Override
    public StatusNotification addProduct(ProductDto productDto) {
        HttpEntity<ProductDto> request = new HttpEntity<>(productDto);
        try {
            log.info("string from restTemplate -> {} ", restTemplate.getUriTemplateHandler());
            ResponseEntity<ProductDto> productDtoResponseEntity = restTemplate.postForEntity(resourceUrl, request, ProductDto.class);
            if (HttpStatus.CREATED.equals(productDtoResponseEntity.getStatusCode())) {
                log.info("addProduct() -> saved > {} ", productDto);
                return StatusNotification.SAVED;
            }
        } catch (Exception e) {
            log.error("addProduct() -> {} ", e.getLocalizedMessage());
        }
        return StatusNotification.ERROR;
    }

    @Override
    public ProductList findAll() {
        try {
            ResponseEntity<?> productListResponseEntity = restTemplate.getForEntity(resourceUrl, List.class);
            if (HttpStatus.OK.equals(productListResponseEntity.getStatusCode())) {
                log.info("findAll() -> products retrieved from backEnd");
                return new ProductList((List<Product>) productListResponseEntity.getBody());
            }
        } catch (Exception e) {
            log.error("findAll() -> {} ", e.getLocalizedMessage());
        }
        return new ProductList(Collections.emptyList());
    }

    @Override
    public Product findById(Long id) {
        try {
            ResponseEntity<?> productResponseEntity = restTemplate.getForEntity(resourceUrl + "/details/" + id, Product.class);
            if (HttpStatus.OK.equals(productResponseEntity.getStatusCode())) {
                log.info("findById() -> product retrieved from backEnd");
                return (Product) productResponseEntity.getBody();
            }
        } catch (Exception e) {
            log.error("findById() -> {} ", e.getLocalizedMessage());
        }
        return new Product();
    }
}
