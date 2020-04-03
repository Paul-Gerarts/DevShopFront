package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.AdminFunctions;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import be.syntra.devshop.DevshopFront.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/addproduct")
    public String displayAddProductsForm(Model model) {
        ProductDto emptyProductDto = productService.createEmptyProduct();
        model.addAttribute("product", emptyProductDto);
        return "admin/product/addProduct";
    }

    @PostMapping("/addproduct")
    public String getProductEntry(@ModelAttribute("product") ProductDto productDto, Model model) {
        StatusNotification statusNotification = productService.addProduct(productDto);
        model.addAttribute("product", productDto);
        model.addAttribute("status", statusNotification);
        return "admin/product/addProduct";
    }

    @GetMapping("/overview")
    public String displayAdminOverview(Model model) {
        List<AdminFunctions> functionList = Arrays.asList(AdminFunctions.values());
        model.addAttribute("functions", functionList);
        return "admin/product/adminOverview";
    }
}
