package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class ProductListCacheServiceImpl implements ProductListCacheService {
    private ProductListCache productListCache;
    private ProductService productService;
    private DataStore dataStore;

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

    private boolean updateCacheToFalse() {
        return dataStore.getMap().put("cacheNeedsUpdate", false);
    }

    private boolean checkIfProductsCacheNeedsUpdate() {
        return dataStore.getMap().get("cacheNeedsUpdate");
    }

    @Override
    public ProductList findBySearchRequest(String searchRequest) {
        var result = getProductListCache()
                .getProducts().stream()
                .filter(product ->
                        product.getName()
                                .toLowerCase()
                                .contains(searchRequest.toLowerCase()))
                .collect(Collectors.toUnmodifiableList());
        return result.isEmpty() ? new ProductList(getProductListCache().getProducts()) : new ProductList(result);
    }

    @Override
    public Product findById(Long id) {
        return getProductListCache().getProducts()
                .stream()
                .filter(product -> product.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product with " + id + " was not found"));
    }
}
