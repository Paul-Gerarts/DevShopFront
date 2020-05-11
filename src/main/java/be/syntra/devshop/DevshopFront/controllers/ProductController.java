package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;
    private final SearchService searchService;
    private final ProductMapper productMapper;
    private final String PRODUCT_DETAIL = "redirect:/products/details/";

    @Autowired
    public ProductController(
            ProductService productService,
            CartService cartService,
            SearchService searchService,
            ProductMapper productMapper
    ) {
        this.productService = productService;
        this.cartService = cartService;
        this.searchService = searchService;
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
    public String handleGet(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("review", new Review());
        model.addAttribute("product", product);
        model.addAttribute("cart", cartService.getCart());
        return "product/productDetails";
    }

    @PostMapping("/details/{id}")
    public String archiveProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        StatusNotification statusNotification = productService.archiveProduct(product);
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
        return PRODUCT_DETAIL + id;
    }

    @PostMapping("/details/{id}/review")
    public String writeReviewOfProduct(@Valid @ModelAttribute Review review, BindingResult bindingResult, @PathVariable Long id, Model model, Principal user) {
        if (!bindingResult.hasErrors()) {
            model.addAttribute("status", productService.writeReviewOfProduct(id, review, user));
            return PRODUCT_DETAIL + id;
        }
        return PRODUCT_DETAIL + id;
    }
}
