package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SaveStatus;
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

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Value("${baseUrl}")
    private String baseUrl;
    @Value("${productsEndpoint}")
    private String endpoint;
    private String resourceUrl = null;
    private CartService cartService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public ProductServiceImpl(CartService cartService) {
        this.cartService = cartService;
    }

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
            log.info("string from restTemplate -> " + restTemplate.getUriTemplateHandler().toString());
            ResponseEntity<ProductDto> productDtoResponseEntity = restTemplate.postForEntity(resourceUrl, request, ProductDto.class);
            if (HttpStatus.CREATED.equals(productDtoResponseEntity.getStatusCode())) {
                log.info("addProduct() -> saved > " + productDto.toString());
                return SaveStatus.SAVED;
            }
        } catch (Exception e) {
            log.error("addProduct() -> " + e);
        }
        return SaveStatus.ERROR;
    }

    @Override
    public ProductList findAll() {
        try {
            ResponseEntity<?> productListResponseEntity = restTemplate.getForEntity(resourceUrl, List.class);
            if (HttpStatus.OK.equals(productListResponseEntity.getStatusCode())) {
                return new ProductList((List<Product>) productListResponseEntity.getBody());
            }
        } catch (Exception e) {
            log.error("findAll() -> " + e);
        }
        return new ProductList(Collections.emptyList());
    }

    @Override
    public void addToCart(Product product) {
        cartService.addToCart(product);
    }
}
