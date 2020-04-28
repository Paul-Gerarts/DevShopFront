package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
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

    private final ProductService productService;
    private final CartService cartService;
    private final SearchService searchService;

    @Autowired
    public ProductController(
            ProductService productService,
            CartService cartService,
            SearchService searchService
    ) {
        this.productService = productService;
        this.cartService = cartService;
        this.searchService = searchService;
    }

    @GetMapping
    public String displayProductOverview(Model model) {
        List<Product> productList = productService.findAllNonArchived().getProducts();
        searchService.resetSearchModel();
        productService.setPriceFilters(productList);
        model.addAttribute("products", productList);
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @GetMapping("/details/{id}")
    public String handleGet(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
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
        List<Product> products = productService.findBySearchRequest(searchService.getSearchModel()).getProducts();
        List<Product> productList = productService.filterByPrice(products, searchService.getSearchModel()).getProducts();
        model.addAttribute("products", productList);
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
