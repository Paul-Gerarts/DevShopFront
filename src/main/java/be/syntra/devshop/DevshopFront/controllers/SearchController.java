package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductListCacheService;
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
@RequestMapping("/search")
public class SearchController {

    private ProductService productService;
    private CartService cartService;
    private SearchService searchService;
    private ProductListCacheService productListCacheService;

    @Autowired
    public SearchController(ProductService productService, CartService cartService, SearchService searchService, ProductListCacheService productListCacheService) {
        this.productService = productService;
        this.cartService = cartService;
        this.searchService = searchService;
        this.productListCacheService = productListCacheService;
    }

    @GetMapping("/")
    public String showSearchBarResult(@RequestParam String searchRequest, Model model) {
        // todo: (DEV-015) the search string is saved into the SearchModelDto starting here
        searchService.captureSearchedTerm(searchRequest);
        // todo: (DEV-015) the searchTerm is retrieved from the from the SearchModelDto here
        //List<Product> productList = productService.findBySearchRequest(searchService.getSearchDto().getBasicSearchTerm()).getProducts();
        List<Product> productList = productListCacheService.findBySearchRequest(searchService.getSearchDto().getBasicSearchTerm()).getProducts();
        model.addAttribute("products", productList);
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @PostMapping
    public String addSelectedProductToCart(@ModelAttribute("product") Product product, Model model) {
        log.info("(searchPage) chosen product -> " + product);
        //productService.addToCart(product);
        productService.addToCart(productListCacheService.findById(product.getId()));
        // todo: (DEV-015) the searchTerm is retrieved from the from the SearchModelDto here, if 'showSearchBarResult' is changed to not use @RequestParam's the code below can be replaced wiht a redirect to '/search' to use the showSearchBarResult method
        //List<Product> productList = productService.findBySearchRequest(searchService.getSearchDto().getBasicSearchTerm()).getProducts();
        List<Product> productList = productListCacheService.findBySearchRequest(searchService.getSearchDto().getBasicSearchTerm()).getProducts();
        model.addAttribute("products", productList);
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }
}
