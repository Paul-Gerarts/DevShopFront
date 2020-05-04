package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;

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
     * returns a ProductList object
     * @return ProductList
     */
    ProductList findAllNonArchived();

    ProductList findAllArchived();

    Product findById(Long id);

    ProductList findAllWithCorrespondingCategory(Long id);

    StatusNotification archiveProduct(Product product);

    /***
     * adds a poduct to the cart
     * @param product
     */
    void addToCart(Product product);

    CategoryList findAllCategories();
}
