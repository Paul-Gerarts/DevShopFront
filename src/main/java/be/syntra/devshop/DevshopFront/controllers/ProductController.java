package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String displayProductOverview(Model model) {
        List<Product> productList = productService.findAllNonArchived().getProducts();
        model.addAttribute("products", productList);
        return "product/productOverview";
    }

    @GetMapping("/details/{id}")
    public String handleGet(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
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
}
