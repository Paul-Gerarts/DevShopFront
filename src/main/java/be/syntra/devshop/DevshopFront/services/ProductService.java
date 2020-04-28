package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.CategoryList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductListDto;

import java.util.List;

public interface ProductService {

    /***
     * creates an empty Dto of a Product obj
     * to use in the form when creating a new Product
     * @return productDto
     */
    ProductDto createEmptyProduct();

    /***
     * to send the new ProductDto to a RESTfull service
     * and return a status that corresponds to the success of the action
     * @param productDto
     */
    StatusNotification addProduct(ProductDto productDto);

    /***
     * to retrieve all products present from the backend
     * returns a ProductListDto object
     * @return ProductListDto
     */
    ProductListDto findAllNonArchived();

    ProductListDto findAllArchived();

    Product findById(Long id);

    StatusNotification archiveProduct(Product product);

    /***
     * adds a poduct to the cart
     * @param product
     */
    void addToCart(Product product);

    CategoryList findAllCategories();

    ProductListDto findBySearchRequest(SearchModel searchModel);

    ProductListDto sortListByName(List<Product> products, SearchModel searchModel);

    ProductListDto sortListByPrice(List<Product> products, SearchModel searchModel);

    ProductListDto filterByPrice(List<Product> products, SearchModel searchModel);

    ProductListDto searchForProductDescription(List<Product> products, SearchModel searchModel);

    void setPriceFilters(List<Product> products);

}
