package be.syntra.devshop.DevshopFront.controller;

import be.syntra.devshop.DevshopFront.model.dto.ProductDto;
import be.syntra.devshop.DevshopFront.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/devshop/admin")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/addproduct")
    public ModelAndView displayAddProductsFrom() {
        return new ModelAndView("admin/product/addProduct", "product", productService.createEmptyProduct());
    }

    @PostMapping("addproduct")
    public ModelAndView getProductEntry(@ModelAttribute("product") ProductDto productDto) {
        System.out.println(productDto);
        productService.addProduct(productDto);
        return new ModelAndView();
    }
}
