package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/devshop/products")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String displayProductOverview(Model model) {
        List<Product> productList = productService.findAll().getProductList();
        model.addAttribute("products", productList);
        return "product/productOverview";
    }

    @PostMapping
    public String addSelectedProductToCart(@ModelAttribute("id") Integer id) {
        System.out.println("chosen product -> " + id);
        return "redirect:/devshop/products";
    }
}
