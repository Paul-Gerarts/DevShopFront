package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductListCacheServiceImpl implements ProductListCacheService {

    private final ProductListCache productListCache;
    private final ProductService productService;
    private final DataStore dataStore;

    @Autowired
    public ProductListCacheServiceImpl(ProductListCache productListCache, ProductService productService, DataStore dataStore) {
        this.productListCache = productListCache;
        this.productService = productService;
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
        return result.isEmpty()
                ? new ProductList(getProductListCache().getProducts())
                : new ProductList(result);
    }

    private List<Product> executeSearch(String searchRequest) {
        return (null != searchRequest)
                ? getProductListCache().getProducts()
                .stream()
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
