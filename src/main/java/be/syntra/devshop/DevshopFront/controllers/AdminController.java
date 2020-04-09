package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.AdminFunctions;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import be.syntra.devshop.DevshopFront.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static be.syntra.devshop.DevshopFront.services.utils.ProductMapperUtil.convertToProductDto;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private static final String PRODUCT_FORM = "admin/product/addProduct";
    private static final String PRODUCT = "product";

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/addproduct")
    public String displayAddProductsForm(Model model) {
        ProductDto emptyProductDto = productService.createEmptyProduct();
        model.addAttribute(PRODUCT, emptyProductDto);
        return PRODUCT_FORM;
    }

    @PostMapping("/addproduct")
    public String getProductEntry(@ModelAttribute("product") ProductDto productDto, Model model) {
        return handleProductForm(productDto, model);
    }

    @GetMapping("/overview")
    public String displayAdminOverview(Model model) {
        List<AdminFunctions> functionList = Arrays.asList(AdminFunctions.values());
        model.addAttribute("functions", functionList);
        return "admin/product/adminOverview";
    }

    @GetMapping("/product/{id}/edit")
    public String forward(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute(PRODUCT, convertToProductDto(product));
        return PRODUCT_FORM;
    }

    @PostMapping("product/{id}/edit")
    public String getUpdatedProduct(@ModelAttribute("product") ProductDto productDto, Model model) {
        return handleProductForm(productDto, model);
    }

    @GetMapping("/archived")
    public String displayArchivedProducts(Model model) {
        List<Product> productList = productService.findAllArchived();
        model.addAttribute("products", productList);
        return "product/productOverview";
    }

    private String handleProductForm(ProductDto productDto, Model model) {
        StatusNotification statusNotification = productService.addProduct(productDto);
        model.addAttribute("products", productDto);
        model.addAttribute("status", statusNotification);
        return PRODUCT_FORM;
    }
}
