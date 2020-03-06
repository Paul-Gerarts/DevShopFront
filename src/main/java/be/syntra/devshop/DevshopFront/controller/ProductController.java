package be.syntra.devshop.DevshopFront.controller;

import be.syntra.devshop.DevshopFront.model.SaveStatus;
import be.syntra.devshop.DevshopFront.model.dto.ProductDto;
import be.syntra.devshop.DevshopFront.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/devshop/admin")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/addproduct")
    public String displayAddProductsFrom(Model model) {
        ProductDto emptyProductDto = productService.createEmptyProduct();
        model.addAttribute("product", emptyProductDto);
        return "admin/product/addProduct";
    }

    @PostMapping("addproduct")
    public String getProductEntry(@ModelAttribute("product") ProductDto productDto, Model model) {
        System.out.println(productDto);
        SaveStatus saveStatus = productService.addProduct(productDto);
        model.addAttribute("product", productDto);
        model.addAttribute("status", saveStatus);
        return "admin/product/addProduct";
    }
}