package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.ProductListCache;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dto.ProductList;

import java.util.List;

public interface ProductListCacheService {

    void updateProductListCache();

    ProductListCache getProductListCache();

    ProductList findBySearchRequest(SearchModel searchModel);

    ProductList sortListByName(List<Product> products, SearchModel searchModel);

    ProductList sortListByPrice(List<Product> products, SearchModel searchModel);

    Product findById(Long id);
}
