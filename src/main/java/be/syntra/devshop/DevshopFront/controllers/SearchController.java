package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dto.SearchDto;
import be.syntra.devshop.DevshopFront.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private ProductService productService;

    @Autowired
    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{searchRequest}")
    public String showSearchBarResult(@PathVariable String searchRequest, Model model) {
        List<Product> productList = productService.findBySearchRequest(searchRequest).getProductList();
        model.addAttribute("products", productList);
        model.addAttribute("search", new SearchDto());
        return "product/productOverview";
    }
}