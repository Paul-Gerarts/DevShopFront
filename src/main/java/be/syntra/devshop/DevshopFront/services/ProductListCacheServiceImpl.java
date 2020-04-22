package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProductListCacheServiceImpl implements ProductListCacheService {

    private final ProductListCache productListCache;
    private final ProductService productService;
    private final SearchService searchService;
    private final DataStore dataStore;

    @Autowired
    public ProductListCacheServiceImpl(
            ProductListCache productListCache,
            ProductService productService,
            SearchService searchService,
            DataStore dataStore
    ) {
        this.productListCache = productListCache;
        this.productService = productService;
        this.searchService = searchService;
        this.dataStore = dataStore;
    }

    @Override
    public void updateProductListCache() {
        productListCache.setProducts(productService.findAllNonArchived().getProducts());
    }

    @Override
    public ProductListCache getProductListCache() {
        if (checkIfProductsCacheNeedsUpdate()) {
            updateCacheToFalse();
            updateProductListCache();
        }
        return productListCache;
    }

    private void updateCacheToFalse() {
        dataStore.getMap().put("cacheNeedsUpdate", false);
    }

    private boolean checkIfProductsCacheNeedsUpdate() {
        return dataStore.getMap().get("cacheNeedsUpdate");
    }

    @Override
    public ProductList findBySearchRequest(SearchModel searchModel) {
        List<Product> result = executeSearch(searchModel.getSearchRequest());
        return getSearchResultsOrAllProducts(result);
    }

    @Override
    public void setPriceFilters(List<Product> products) {
        searchService.getSearchModel().setSortAscendingPrice(true);
        List<Product> sortedProducts = sortListByPrice(products, searchService.getSearchModel()).getProducts();
        BigDecimal priceLow = new BigDecimal("0");
        BigDecimal priceHigh = sortedProducts.get(sortedProducts.size() - 1).getPrice();
        searchService.setPriceLow(priceLow);
        searchService.setPriceHigh(priceHigh);
    }

    @Override
    public ProductList filterByPrice(List<Product> products, SearchModel searchModel) {
        setPriceFiltersToSearchModel(searchModel);
        List<Product> result = products
                .parallelStream()
                .filter(applyPriceFilter(searchModel))
                .collect(Collectors.toUnmodifiableList());
        return getSearchResultsOrAllProducts(result);
    }

    private ProductList getSearchResultsOrAllProducts(List<Product> result) {
        searchService.setSearchFailure(result.isEmpty());
        return result.isEmpty()
                ? new ProductList(getProductListCache().getProducts())
                : new ProductList(result);
    }

    private void setPriceFiltersToSearchModel(SearchModel searchModel) {
        searchModel.setPriceFilters(" with the applied filters");
        String searchRequest = (null != searchService.getSearchModel().getSearchRequest())
                ? searchService.getSearchModel().getSearchRequest()
                : "";
        searchModel.setSearchRequest(searchRequest);
        searchModel.setSearchFailure(false);
        searchModel.setActiveFilters(true);
    }

    private Predicate<Product> applyPriceFilter(SearchModel searchModel) {
        return product -> product.getPrice().compareTo(searchModel.getPriceLow()) >= 0 && product.getPrice().compareTo(searchModel.getPriceHigh()) <= 0;
    }

    @Override
    public ProductList sortListByName(List<Product> products, SearchModel searchModel) {
        final Comparator<Product> productNameComparator = (searchModel.isSortAscendingName())
                ? Comparator.comparing(Product::getName)
                : Comparator.comparing(Product::getName).reversed();
        return getSortedList(products, productNameComparator);
    }

    @Override
    public ProductList sortListByPrice(List<Product> products, SearchModel searchModel) {
        final Comparator<Product> productPriceComparator = (searchModel.isSortAscendingPrice())
                ? Comparator.comparing(Product::getPrice)
                : Comparator.comparing(Product::getPrice).reversed();
        return getSortedList(products, productPriceComparator);
    }

    private ProductList getSortedList(List<Product> products, Comparator<Product> productComparator) {
        return new ProductList(
                products
                        .stream()
                        .sorted(productComparator)
                        .collect(Collectors.toUnmodifiableList()));
    }

    private List<Product> executeSearch(String searchRequest) {
        return (null != searchRequest)
                ? getProductListCache().getProducts()
                .parallelStream()
                .filter(product -> product.getName()
                        .toLowerCase()
                        .contains(searchRequest.toLowerCase()))
                .collect(Collectors.toUnmodifiableList())
                : new ArrayList<>();
    }

    @Override
    public Product findById(Long id) {
        return getProductListCache().getProducts()
                .stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
