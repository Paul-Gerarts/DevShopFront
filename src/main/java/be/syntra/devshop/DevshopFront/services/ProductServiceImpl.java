package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.CategoryNotFoundException;
import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.ReviewDto;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapperUtil;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
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
import java.security.Principal;
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
    private final ProductMapper productMapper;
    private final SearchService searchService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public ProductServiceImpl(
            CartService cartService,
            DataStore dataStore,
            ProductMapper productMapper,
            SearchService searchService
    ) {
        this.cartService = cartService;
        this.dataStore = dataStore;
        this.productMapper = productMapper;
        this.searchService = searchService;
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
        ResponseEntity<ProductDto> productDtoResponseEntity = restTemplate.postForEntity(resourceUrl, productDto, ProductDto.class);
        if (HttpStatus.CREATED.equals(productDtoResponseEntity.getStatusCode())) {
            log.info("addProduct() -> saved > {} ", productDto);
            return StatusNotification.SAVED;
        }
        return StatusNotification.FORM_ERROR;
    }

    @Override
    public ProductList findAllProductsBySearchModel() {
        ResponseEntity<ProductList> productListResponseEntity = restTemplate.postForEntity(resourceUrl + "/searching/", searchService.getSearchModel(), ProductList.class);
        if (HttpStatus.OK.equals(productListResponseEntity.getStatusCode())) {
            log.info("findAllProductsBySearchModel -> receivedFromBackEnd");
            return productListResponseEntity.getBody();
        }
        return new ProductList(Collections.emptyList());
    }

    @Override
    public ProductList findAllWithOnlyCategory(Long id) {
        ResponseEntity<ProductList> productListResponseEntity = restTemplate.getForEntity(resourceUrl + "/all/" + id, ProductList.class);
        if (HttpStatus.OK.equals(productListResponseEntity.getStatusCode())) {
            log.info("findAllWithCorrespondingCategory -> receivedFromBackEnd");
            return productListResponseEntity.getBody();
        }
        return new ProductList(Collections.emptyList());
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
    public StatusNotification writeReviewOfProduct(Long productId, Review review, Principal user) {
        ReviewDto reviewDto = getBuild(productId, review, user);
        HttpEntity<ReviewDto> request = new HttpEntity<>(reviewDto);
        ResponseEntity<ReviewDto> reviewDtoResponseEntity = restTemplate.postForEntity(resourceUrl + "/reviewProduct", request, ReviewDto.class);
        if (HttpStatus.CREATED.equals(reviewDtoResponseEntity.getStatusCode())) {
            log.info("updateProduct() -> saved > {} ", reviewDto);
            return StatusNotification.SUCCESS;
        }
        return StatusNotification.ERROR;
    }

    private ReviewDto getBuild(Long productId, Review review, Principal user) {
        return ReviewDto.builder()
                .review(review.getReviewText())
                .productName(findById(productId).getName())
                .userName(user.getName())
                .build();
    }

    @Override
    public StatusNotification archiveProduct(Product product) {
        product.setArchived(true);
        ProductDto productDto = productMapper.convertToProductDto(product);
        HttpEntity<ProductDto> request = new HttpEntity<>(productDto);
        ResponseEntity<ProductDto> productResponseEntity = restTemplate.postForEntity(resourceUrl + "/update", request, ProductDto.class);
        if (HttpStatus.CREATED.equals(productResponseEntity.getStatusCode())) {
            log.info("updateProduct() -> saved > {} ", product);
            return StatusNotification.UPDATED;
        }
        return StatusNotification.FORM_ERROR;
    }

    @Override
    public void addToCart(Product product) {
        cartService.addToCart(product);
    }
}
