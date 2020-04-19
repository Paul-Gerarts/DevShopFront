package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;

public interface ProductListCacheService {

    void updateProductListCache();

    ProductListCache getProductListCache();

    ProductList findBySearchRequest(SearchModel searchModel);

    Product findById(Long id);
}
