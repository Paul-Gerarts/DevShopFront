package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductListCacheService;
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

    private final CartService cartService;
    private final SearchService searchService;
    private final ProductListCacheService productListCacheService;
    private static final String PRODUCTS = "products";
    private static final String PRODUCT_OVERVIEW = "product/productOverview";

    @Autowired
    public SearchController(
            CartService cartService,
            SearchService searchService,
            ProductListCacheService productListCacheService
    ) {
        this.cartService = cartService;
        this.searchService = searchService;
        this.productListCacheService = productListCacheService;
    }

    @GetMapping("/")
    public String showSearchBarResult(@RequestParam String searchRequest, Model model) {
        searchService.setSearchRequest(searchRequest);
        List<Product> productList = productListCacheService.findBySearchRequest(searchService.getSearchModel()).getProducts();
        model.addAttribute(PRODUCTS, productList);
        model.addAttribute("cart", cartService.getCart());
        return PRODUCT_OVERVIEW;
    }

    @GetMapping("/sortbyname")
    public String sortByName(Model model) {
        List<Product> productList = productListCacheService.findBySearchRequest(searchService.getSearchModel()).getProducts();
        List<Product> sortedProducts = productListCacheService.sortListByName(productList, searchService.getSearchModel()).getProducts();
        reverseNameSort();
        model.addAttribute(PRODUCTS, sortedProducts);
        model.addAttribute("cart", cartService.getCart());
        return PRODUCT_OVERVIEW;
    }

    @GetMapping("/sortbyprice")
    public String sortByPrice(Model model) {
        List<Product> productList = productListCacheService.findBySearchRequest(searchService.getSearchModel()).getProducts();
        List<Product> sortedProducts = productListCacheService.sortListByPrice(productList, searchService.getSearchModel()).getProducts();
        reversePriceSort();
        model.addAttribute(PRODUCTS, sortedProducts);
        model.addAttribute("cart", cartService.getCart());
        return PRODUCT_OVERVIEW;
    }

    private void reverseNameSort() {
        boolean sortAscending = searchService.getSearchModel().isSortAscendingName();
        searchService.getSearchModel().setSortAscendingName(!sortAscending);
    }

    private void reversePriceSort() {
        boolean sortAscending = searchService.getSearchModel().isSortAscendingPrice();
        searchService.getSearchModel().setSortAscendingPrice(!sortAscending);
    }

}
