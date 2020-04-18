package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;

public interface ProductListCacheService {

    void updateProductListCache();

    ProductListCache getProductListCache();

    ProductList findBySearchRequest(String searchRequest);

    Product findById(Long id);
}
