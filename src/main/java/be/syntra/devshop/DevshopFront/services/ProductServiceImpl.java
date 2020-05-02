package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.CategoryNotFoundException;
import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.Collections;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${productsEndpoint}")
    private String endpoint;

    private String resourceUrl = null;

    private final CartService cartService;
    private final DataStore dataStore;
    private final ProductMapperUtil productMapperUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public ProductServiceImpl(
            CartService cartService,
            DataStore dataStore,
            ProductMapperUtil productMapperUtil
    ) {
        this.cartService = cartService;
        this.dataStore = dataStore;
        this.productMapperUtil = productMapperUtil;
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
    public StatusNotification addProduct(@Valid ProductDto productDto) {
        HttpEntity<ProductDto> request = new HttpEntity<>(productDto);
        log.info("string from restTemplate -> {} ", restTemplate.getUriTemplateHandler());
        ResponseEntity<ProductDto> productDtoResponseEntity = restTemplate.postForEntity(resourceUrl, request, ProductDto.class);
        dataStore.getMap().put("cacheNeedsUpdate", true);
        if (HttpStatus.CREATED.equals(productDtoResponseEntity.getStatusCode())) {
            log.info("addProduct() -> saved > {} ", productDto);
            return StatusNotification.SAVED;
        }
        return StatusNotification.FORM_ERROR;
    }

    @Override
    public ProductList findAllNonArchived() {
        return retrieveProductListFrom(resourceUrl);
    }

    @Override
    public ProductList findAllArchived() {
        return retrieveProductListFrom(resourceUrl + "/archived");
    }

    @Override
    public Product findById(Long id) {
        ResponseEntity<Product> productResponseEntity = restTemplate.getForEntity(resourceUrl + "/details/" + id, Product.class);
        if (HttpStatus.OK.equals(productResponseEntity.getStatusCode())) {
            log.info("findById() -> product retrieved from backEnd");
            return productResponseEntity.getBody();
        } else if (HttpStatus.NOT_FOUND.equals(productResponseEntity.getStatusCode())) {
            throw new ProductNotFoundException("Product with id: " + id + " was not found");
        }
        return new Product();
    }

    @Override
    public CategoryList findAllCategories() {
        ResponseEntity<CategoryList> categoryListResponseEntity = restTemplate.getForEntity(resourceUrl + "/categories", CategoryList.class);
        if (HttpStatus.OK.equals(categoryListResponseEntity.getStatusCode())) {
            log.info("findAllCategories() -> {}", categoryListResponseEntity.getBody());
            return categoryListResponseEntity.getBody();
        } else if (HttpStatus.NOT_FOUND.equals(categoryListResponseEntity.getStatusCode())) {
            throw new CategoryNotFoundException("No categories found");
        }
        return new CategoryList();
    }

    @Override
    public StatusNotification archiveProduct(Product product) {
        product.setArchived(true);
        ProductDto productDto = productMapperUtil.convertToProductDto(product);
        HttpEntity<ProductDto> request = new HttpEntity<>(productDto);
        ResponseEntity<ProductDto> productResponseEntity = restTemplate.postForEntity(resourceUrl + "/update", request, ProductDto.class);
        if (HttpStatus.CREATED.equals(productResponseEntity.getStatusCode())) {
            log.info("updateProduct() -> saved > {} ", product);
            return StatusNotification.UPDATED;
        }
        return StatusNotification.FORM_ERROR;
    }

    private ProductList retrieveProductListFrom(String resourceUrl) {
        ResponseEntity<ProductList> productListResponseEntity = restTemplate.getForEntity(resourceUrl, ProductList.class);
        if (HttpStatus.OK.equals(productListResponseEntity.getStatusCode())) {
            log.info("findProductList() -> products retrieved from backEnd");
            return productListResponseEntity.getBody();
        }
        return new ProductList(Collections.emptyList());
    }

    @Override
    public void addToCart(Product product) {
        cartService.addToCart(product);
    }
}
