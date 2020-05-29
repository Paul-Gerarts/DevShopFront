package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.CategoryNotFoundException;
import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Value("${baseUrl}")
    private String baseUrl;

    @Value("${productsEndpoint}")
    private String endpoint;

    private String resourceUrl = null;

    private final CartService cartService;
    private final SearchService searchService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public ProductServiceImpl(
            CartService cartService,
            SearchService searchService
    ) {
        this.cartService = cartService;
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
    public void addSelectableCategoriesToSearchModel() {
        this.searchService.getSearchModel().setCategories(findAllCategories().getCategories());
    }

    @Override
    public StatusNotification addProduct(@Valid ProductDto productDto) {
        ResponseEntity<ProductDto> productDtoResponseEntity = restTemplate.postForEntity(resourceUrl, productDto, ProductDto.class);
        if (HttpStatus.CREATED.equals(productDtoResponseEntity.getStatusCode())) {
            log.info("addProduct() -> saved -> {} ", productDto);
            return StatusNotification.SAVED;
        }
        return StatusNotification.FORM_ERROR;
    }

    @Override
    public ProductList findAllProductsBySearchModel() {
        log.info("findAllProductsBySearchModel() -> {}", searchService.getSearchModel());
        ResponseEntity<ProductList> productListResponseEntity = restTemplate.postForEntity(resourceUrl + "/searching/", wrap(searchService.getSearchModel()), ProductList.class);
        if (HttpStatus.OK.equals(productListResponseEntity.getStatusCode())) {
            checkResultForSearchFailure(productListResponseEntity);
            return productListResponseEntity.getBody();
        }
        return ProductList.builder()
                .products(Collections.emptyList())
                .build();
    }

    /*
     * when using the raw searchModel, a stackOverFlowError is thrown
     * instead, we're wrapping our SearchModel to be able to persist without a problem
     * @Return: SearchModel which is a copy of the active searchModel
     */
    private SearchModel wrap(SearchModel searchModel) {
        return SearchModel.builder()
                .archivedView(searchModel.isArchivedView())
                .searchRequest(searchModel.getSearchRequest())
                .searchResultView(searchModel.isSearchResultView())
                .searchFailure(searchModel.isSearchFailure())
                .activeFilters(searchModel.isActiveFilters())
                .appliedFiltersHeader(searchModel.getAppliedFiltersHeader())
                .description(searchModel.getDescription())
                .nameSortActive(searchModel.isNameSortActive())
                .priceHigh(searchModel.getPriceHigh())
                .priceLow(searchModel.getPriceLow())
                .priceSortActive(searchModel.isPriceSortActive())
                .sortAscendingName(searchModel.isSortAscendingName())
                .sortAscendingPrice(searchModel.isSortAscendingPrice())
                .pageNumber(searchModel.getPageNumber())
                .pageSize(searchModel.getPageSize())
                .selectedCategories(searchModel.getSelectedCategories())
                .build();
    }

    private void checkResultForSearchFailure(ResponseEntity<ProductList> productListResponseEntity) {
        if (null != productListResponseEntity.getBody()) {
            searchService.getSearchModel().setSearchFailure(Objects.requireNonNull(productListResponseEntity.getBody()).isSearchFailure());
        } else {
            searchService.getSearchModel().setSearchFailure(true);
        }
    }

    @Override
    public ProductList findAllWithOnlyCategory(Long id) {
        ResponseEntity<ProductList> productListResponseEntity = restTemplate.getForEntity(resourceUrl + "/all/" + id, ProductList.class);
        if (HttpStatus.OK.equals(productListResponseEntity.getStatusCode())) {
            return productListResponseEntity.getBody();
        }
        return ProductList.builder().products(Collections.emptyList()).build();
    }

    @Override
    public Product findById(Long id) {
        ResponseEntity<Product> productResponseEntity = restTemplate.getForEntity(resourceUrl + "/details/" + id, Product.class);
        if (HttpStatus.OK.equals(productResponseEntity.getStatusCode())) {
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
            return categoryListResponseEntity.getBody();
        } else if (HttpStatus.NOT_FOUND.equals(categoryListResponseEntity.getStatusCode())) {
            throw new CategoryNotFoundException("No categories found");
        }
        return new CategoryList();
    }

    @Override
    public StatusNotification archiveProduct(ProductDto product) {
        ResponseEntity<ProductDto> productResponseEntity = restTemplate.postForEntity(resourceUrl + "/update", product, ProductDto.class);
        if (HttpStatus.CREATED.equals(productResponseEntity.getStatusCode())) {
            log.info("updateProduct() -> saved -> {} ", product);
            return StatusNotification.UPDATED;
        }
        return StatusNotification.FORM_ERROR;
    }

    @Override
    public void addToCart(Product product) {
        cartService.addToCart(product);
    }

    @Override
    public StarRatingSet getRatingsFromProduct(Long productId) {
        ResponseEntity<StarRatingSet> starRatingSetResponseEntity = restTemplate.getForEntity(resourceUrl + "/ratings/" + productId, StarRatingSet.class);
        if (HttpStatus.OK.equals(starRatingSetResponseEntity.getStatusCode())) {
            log.info("getRatingsFromProduct() -> {}", starRatingSetResponseEntity.getBody());
            return starRatingSetResponseEntity.getBody();
        }
        return new StarRatingSet(Collections.emptySet());
    }
}
