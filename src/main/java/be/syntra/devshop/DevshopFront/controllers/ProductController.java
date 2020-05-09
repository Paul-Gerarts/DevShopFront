package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductListCacheService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;
    private final SearchService searchService;
    private final ProductListCacheService productListCacheService;

    @Autowired
    public ProductController(
            ProductService productService,
            CartService cartService,
            SearchService searchService,
            ProductListCacheService productListCacheService
    ) {
        this.productService = productService;
        this.cartService = cartService;
        this.searchService = searchService;
        this.productListCacheService = productListCacheService;
    }

    @GetMapping
    public String displayProductOverview(Model model) {
        List<Product> productList = productListCacheService.getProductListCache().getProducts();
        searchService.resetSearchModel();
        productListCacheService.setPriceFilters(productList);
        model.addAttribute("products", productList);
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @GetMapping("/details/{id}")
    public String handleGet(@PathVariable Long id, Model model) {
        Product product =
                isProductListCacheEmpty(id)
                        ? productService.findById(id)
                        : productListCacheService.findById(id);
        model.addAttribute("review", new Review());
        model.addAttribute("product", product);
        model.addAttribute("cart", cartService.getCart());
        return "product/productDetails";
    }

    private boolean isProductListCacheEmpty(Long id) {
        return null == productListCacheService.findById(id);
    }

    @PostMapping("/details/{id}")
    public String archiveProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        StatusNotification statusNotification = productService.archiveProduct(product);
        productListCacheService.updateProductListCache();
        model.addAttribute("product", product);
        model.addAttribute("status", statusNotification);
        return "product/productDetails";
    }

    @PostMapping
    public String addSelectedProductToCart(@ModelAttribute("id") Long id, Model model) {
        log.info("addSelectedProductToCart()-> {}", id);
        productService.addToCart(productListCacheService.findById(id));
        List<Product> products = productListCacheService.findBySearchRequest(searchService.getSearchModel()).getProducts();
        List<Product> productList = productListCacheService.filterByPrice(products, searchService.getSearchModel()).getProducts();
        model.addAttribute("products", productList);
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @PostMapping("/details/addtocart/{id}")
    public String addSelectedProductFromDetailToCart(@PathVariable Long id) {
        log.info("addSelectedProductFromDetailToCart()-> {}", id);
        productService.addToCart(productListCacheService.findById(id));
        return "redirect:/products/details/" + id;
    }

    @PostMapping("/details/{id}/review")
    public String writeReviewOfProduct(@Valid @ModelAttribute Review review, BindingResult bindingResult, @PathVariable Long id, Model model, Principal user) {
        if (!bindingResult.hasErrors()) {
            productService.writeReviewOfProduct(id, review, user);
            productListCacheService.updateProductListCache();
        }
        return "redirect:/products/details/" + id;
    }
}
