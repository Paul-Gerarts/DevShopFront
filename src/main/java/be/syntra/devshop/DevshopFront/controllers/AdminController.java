package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.AdminFunctions;
import be.syntra.devshop.DevshopFront.models.Category;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final SearchService searchService;
    private final ProductMapperUtil productMapperUtil;
    private static final String PRODUCT_FORM = "admin/product/addProduct";
    private static final String PRODUCT = "product";

    @Autowired
    public AdminController(
            ProductService productService,
            SearchService searchService,
            ProductMapperUtil productMapperUtil
    ) {
        this.productService = productService;
        this.searchService = searchService;
        this.productMapperUtil = productMapperUtil;
    }

    @GetMapping("/addproduct")
    public String displayAddProductsForm(Model model) {
        ProductDto emptyProductDto = productService.createEmptyProduct();
        addCategoriesModel(model);
        model.addAttribute(PRODUCT, emptyProductDto);
        return PRODUCT_FORM;
    }

    @PostMapping("/addproduct")
    public String getProductEntry(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult bindingResult, Model model) {
        return handleChangedProductForm(productDto, bindingResult, model);
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
        addCategoriesModel(model);
        model.addAttribute(PRODUCT, productMapperUtil.convertToProductDto(product));
        return PRODUCT_FORM;
    }

    @PostMapping("product/{id}/edit")
    public String getUpdatedProduct(@ModelAttribute("product") @Valid ProductDto productDto, BindingResult bindingResult, Model model) {
        return handleChangedProductForm(productDto, bindingResult, model);
    }

    @GetMapping("/archived")
    public String displayArchivedProducts(Model model) {
        searchService.resetSearchModel();
        searchService.setSearchResultView(false);
        searchService.setArchivedView(true);
        ProductList productList = productService.findAllProductsBySearchModel();
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("productlist", productMapperUtil.convertToProductDtoList(productList));
        return "product/productOverview";
    }

    private String handleChangedProductForm(@ModelAttribute("product") @Valid ProductDto productDto, BindingResult bindingResult, Model model) {
        addCategoriesModel(model);
        return (!bindingResult.hasErrors())
                ? handleProductForm(productDto, model)
                : PRODUCT_FORM;
    }

    private void addCategoriesModel(Model model) {
        List<Category> categories = productService.findAllCategories().getCategories();
        model.addAttribute("categories", categories);
    }

    private String handleProductForm(ProductDto productDto, Model model) {
        StatusNotification statusNotification = productService.addProduct(productDto);
        model.addAttribute("products", productDto);
        model.addAttribute("status", statusNotification);
        return PRODUCT_FORM;
    }
}
