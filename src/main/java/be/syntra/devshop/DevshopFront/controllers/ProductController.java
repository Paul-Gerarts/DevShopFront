package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductListCacheService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    private CartService cartService;
    private ProductListCacheService productListCacheService;

    @Autowired
    public ProductController(ProductService productService, CartService cartService, ProductListCacheService productListCacheService) {
        this.productService = productService;
        this.cartService = cartService;
        this.productListCacheService = productListCacheService;
    }

    @GetMapping
    public String displayProductOverview(Model model) {
        List<Product> productList = productListCacheService.getProductListCache().getCachedProductList();
        model.addAttribute("products", productList);
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @GetMapping("/details/{id}")
    public String handleGet(@PathVariable Long id, Model model) {
        Product product = productListCacheService.findById(id);
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
    public String addSelectedProductToCart(@ModelAttribute("id") Long id) {
        log.info("chosen product id-> " + id);
        productService.addToCart(productListCacheService.findById(id));
        return "redirect:/products";
    }

    @PostMapping("/details/addtocart/{id}")
    public String addSelectedProductFromDetailToCart(@PathVariable Long id) {
        productService.addToCart(productListCacheService.findById(id));
        return "redirect:/products/details/" + id;
    }
}
