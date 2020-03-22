package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SaveStatus;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ProductServiceImpl implements ProductService {
    @Value("${baseUrl}")
    private String baseUrl;
    @Value("${productsEndpoint}")
    private String endpoint;
    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
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
    public SaveStatus addProduct(ProductDto productDto) {
        HttpEntity<ProductDto> request = new HttpEntity<>(productDto);
        try {
            logger.info("string from restTemplate -> " + restTemplate.getUriTemplateHandler().toString());
            ResponseEntity<ProductDto> productDtoResponseEntity = restTemplate.postForEntity(resourceUrl, request, ProductDto.class);
            if (HttpStatus.CREATED.equals(productDtoResponseEntity.getStatusCode())) {
                logger.info("addProduct() -> saved > " + productDto.toString());
                return SaveStatus.SAVED;
            }
        } catch (Exception e) {
            logger.info("addProduct() -> " + e.getCause().toString());
            logger.info("addProduct() -> " + e.getLocalizedMessage());
        }
        return SaveStatus.ERROR;
    }

    @Override
    public ProductList findAll() {
        try {
            ResponseEntity<?> productListResponseEntity = restTemplate.getForEntity(resourceUrl, List.class);
            if (HttpStatus.OK.equals(productListResponseEntity.getStatusCode())) {
                logger.info("findAll() -> products retrieved from backEnd");
                return new ProductList((List<Product>) productListResponseEntity.getBody());
            }
        } catch (Exception e) {
            logger.error("findAll() -> " + e.getCause().toString());
            logger.error("findAll() -> " + e.getLocalizedMessage());
        }
        return new ProductList(Collections.emptyList());
    }
}
