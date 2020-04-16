package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.ProductNotFoundException;
import be.syntra.devshop.DevshopFront.models.DataStore;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
        productListCache.setCachedProductList(productService.findAllNonArchived().getProducts());
    }

    @Override
    public ProductListCache getProductListCache() {
        if (dataStore.getMap().get("cacheNeedsUpdate")) {
            dataStore.getMap().put("cacheNeedsUpdate", false);
            updateProductListCache();
        }
        return productListCache;
    }

    @Override
    public ProductList findBySearchRequest(String searchRequest) {
        var result = getProductListCache()
                .getCachedProductList().stream()
                .filter(product ->
                        product.getName()
                                .toLowerCase()
                                .contains(searchRequest.toLowerCase()))
                .collect(Collectors.toList());
        return result.isEmpty() ? new ProductList(getProductListCache().getCachedProductList()) : new ProductList(result);
    }

    @Override
    public Product findById(Long id) {
        Optional<Product> productOptional = getProductListCache().getCachedProductList().stream().filter(product -> product.getId() == id).findFirst();
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            throw new ProductNotFoundException("Product with" + id + "was not found");
        }
    }
}
