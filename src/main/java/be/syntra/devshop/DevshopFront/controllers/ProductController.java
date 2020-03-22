package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/addproduct")
    public String displayAddProductsForm(Model model) {
        ProductDto emptyProductDto = productService.createEmptyProduct();
        model.addAttribute("product", emptyProductDto);
        return "admin/product/addProduct";
    }

    @PostMapping("/addproduct")
    public String getProductEntry(@ModelAttribute("product") ProductDto productDto, Model model) {
        SaveStatus saveStatus = productService.addProduct(productDto);
        model.addAttribute("product", productDto);
        model.addAttribute("status", saveStatus);
        return "admin/product/addProduct";
    }
}
