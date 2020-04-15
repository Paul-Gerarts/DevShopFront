package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static java.lang.Boolean.parseBoolean;

@Controller
@RequestMapping("/search")
public class SearchController {

    private ProductService productService;

    @Autowired
    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String showSearchBarResult(@RequestParam String searchRequest, Model model) {
        List<Product> productList = productService.findBySearchRequest(searchRequest).getProducts();
        model.addAttribute("products", productList);
        return "product/productOverview";
    }

    @GetMapping("/filter/{searchRequest}/{sortAsc}")
    public String sortByAlfabeticOrder(@PathVariable String searchRequest, @PathVariable String sortAsc, Model model) {
        List<Product> productList = productService.findBySearchRequestSortByName(searchRequest, parseBoolean(sortAsc)).getProducts();
        model.addAttribute("products", productList);
        return "product/productOverview";
    }
}
