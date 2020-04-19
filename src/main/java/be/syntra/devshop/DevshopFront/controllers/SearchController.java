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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/search")
public class SearchController {

    private final ProductService productService;
    private final CartService cartService;
    private final SearchService searchService;
    private final ProductListCacheService productListCacheService;

    @Autowired
    public SearchController(
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

    @GetMapping("/")
    public String showSearchBarResult(@RequestParam String searchRequest, Model model) {
        searchService.setSearchRequest(searchRequest);
        List<Product> productList = productListCacheService.findBySearchRequest(searchService.getSearchModel()).getProducts();
        model.addAttribute("products", productList);
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

}
