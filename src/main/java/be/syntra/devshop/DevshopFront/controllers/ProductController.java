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
        ProductList productList = productService.findAllProductsBySearchModel();
        searchService.setPriceFilters(productList.getProducts());
        model.addAttribute("productlist", productMapper.convertToProductDtoList(productList));
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @GetMapping("/details/{id}")
    public String handleGet(@PathVariable Long id, Model model, Principal user) {
        Product product = productService.findById(id);
        StarRatingDto rating = starRatingService.findBy(id, user.getName());
        model.addAttribute("product", product);
        model.addAttribute("cart", cartService.getCart());
        return "product/productDetails";
    }

    @PostMapping("/details/{id}")
    public String archiveProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        StatusNotification statusNotification = productService.archiveProduct(productMapper.convertToProductDto(product));
        model.addAttribute("product", product);
        model.addAttribute("status", statusNotification);
        return "product/productDetails";
    }

    @PostMapping
    public String addSelectedProductToCart(@ModelAttribute("id") Long id, Model model) {
        log.info("addSelectedProductToCart()-> {}", id);
        productService.addToCart(productService.findById(id));
        ProductList productList = productService.findAllProductsBySearchModel();
        model.addAttribute("productlist", productMapper.convertToProductDtoList(productList));
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
}
