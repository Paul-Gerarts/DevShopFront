package be.syntra.devshop.DevshopFront.controller;

import be.syntra.devshop.DevshopFront.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/devshop/admin")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/addproduct")
    public ModelAndView displayAddProductsFrom() {
        return new ModelAndView("admin/product/addProduct", "product", productService.createEmptyProduct());
    }
}
