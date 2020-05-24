package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.StarRatingService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;
    private final SearchService searchService;
    private final StarRatingService starRatingService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductController(
            ProductService productService,
            CartService cartService,
            SearchService searchService,
            StarRatingService starRatingService,
            ProductMapper productMapper
    ) {
        this.productService = productService;
        this.cartService = cartService;
        this.searchService = searchService;
        this.starRatingService = starRatingService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public String displayProductOverview(Model model) {
        searchService.resetSearchModel();
        productService.addSelectableCategoriesToSearchModel();
        ProductList productList = productService.findAllProductsBySearchModel();
        searchService.setPriceFilters(BigDecimal.ZERO, productList.getSearchResultMaxPrice());
        model.addAttribute("productlist", productMapper.convertToProductsDisplayListDto(productList));
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @GetMapping("/details/{id}")
    public String getProductDetailsById(@PathVariable Long id, Model model, Principal user) {
        return getProductDetails(id, model, user);
    }

    @PostMapping("/details/{id}")
    public String archiveProduct(@PathVariable Long id, Model model, Principal user) {
        Product product = productService.findById(id);
        product.setArchived(true);
        StatusNotification statusNotification = productService.archiveProduct(productMapper.convertToProductDto(product));
        StarRatingDto rating = starRatingService.findByUserNameAndId(id, nullSafe(user));
        model.addAttribute("rating", rating);
        model.addAttribute("product", product);
        model.addAttribute("status", statusNotification);
        return "product/productDetails";
    }

    @PostMapping("{productId}/ratings/{count}")
    public String submitRating(@PathVariable Long productId, @PathVariable Double count, Model model, Principal user) {
        StatusNotification status = starRatingService.submitRating(productId, count, nullSafe(user));
        model.addAttribute("status", status);
        return getProductDetails(productId, model, user);
    }

    @PostMapping
    public String addSelectedProductToCart(@ModelAttribute("id") Long id, Model model) {
        log.info("addSelectedProductToCart()-> {}", id);
        productService.addToCart(productService.findById(id));
        ProductList productList = productService.findAllProductsBySearchModel();
        model.addAttribute("productlist", productMapper.convertToProductsDisplayListDto(productList));
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @PostMapping("/details/addtocart/{id}")
    public String addSelectedProductFromDetailToCart(@PathVariable Long id) {
        log.info("addSelectedProductFromDetailToCart()-> {}", id);
        productService.addToCart(productService.findById(id));
        return "redirect:/products/details/" + id;
    }

    private String getProductDetails(Long id, Model model, Principal user) {
        Product product = productService.findById(id);
        StarRatingDto rating = starRatingService.findByUserNameAndId(id, nullSafe(user));
        model.addAttribute("rating", rating);
        model.addAttribute("product", product);
        model.addAttribute("cart", cartService.getCart());
        return "product/productDetails";
    }

    private String nullSafe(Principal user) {
        return null == user
                ? ""
                : user.getName();
    }
}
