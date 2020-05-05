package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.AdminFunctions;
import be.syntra.devshop.DevshopFront.models.Category;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.services.CategoryService;
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
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final SearchService searchService;
    private final CategoryService categoryService;
    private final ProductMapperUtil productMapperUtil;
    private static final String PRODUCT_FORM = "admin/product/addProduct";
    private static final String CATEGORY_FORM = "admin/product/addCategory";
    private static final String PRODUCTS = "products";
    private static final String PRODUCT = "product";
    private static final String STATUS = "status";

    @Autowired
    public AdminController(
            ProductService productService,
            SearchService searchService,
            ProductMapperUtil productMapperUtil,
            CategoryService categoryService
    ) {
        this.productService = productService;
        this.searchService = searchService;
        this.productMapperUtil = productMapperUtil;
        this.categoryService = categoryService;
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
        List<Product> productList = productService.findAllArchived().getProducts();
        searchService.setSearchResultView(false);
        searchService.setArchivedView(true);
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute(PRODUCTS, productList);
        return "product/productOverview";
    }

    @GetMapping("/manage_category")
    public String displayCategoryForm(Model model) {
        addCategoriesModel(model);
        return CATEGORY_FORM;
    }

    @PostMapping("/manage_category/delete/{id}")
    public String removeCategory(@PathVariable Long id, Model model) {
        StatusNotification deletedCategory = categoryService.delete(id);
        handleDeleteFail(id, model, deletedCategory);
        addCategoriesModel(model);
        model.addAttribute(STATUS, deletedCategory);
        model.addAttribute("idToDelete", id);
        return CATEGORY_FORM;
    }

    @PostMapping("/manage_category/set_category/{categoryToDelete}/{categoryToSet}")
    public String setCategoryToProducts(@PathVariable Long categoryToDelete, @PathVariable Long categoryToSet, Model model) {
        StatusNotification deletedCategory = categoryService.setNewCategories(categoryToDelete, categoryToSet);
        StatusNotification succes = StatusNotification.PERSISTENCE_ERROR.equals(deletedCategory)
                ? StatusNotification.DELETE_FAIL
                : categoryService.delete(categoryToDelete);
        addCategoriesModel(model);
        model.addAttribute(STATUS, succes);
        return CATEGORY_FORM;
    }

    @PutMapping("/manage_category/update_category/{categoryToUpdate}/{categoryToSet}")
    public String updateCategory(@PathVariable Long categoryToUpdate, @PathVariable Long categoryToSet, Model model) {
        addCategoriesModel(model);
        model.addAttribute(STATUS, categoryService.updateCategory(categoryToUpdate, categoryToSet));
        return CATEGORY_FORM;
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
        model.addAttribute(PRODUCTS, productDto);
        model.addAttribute(STATUS, statusNotification);
        return PRODUCT_FORM;
    }

    private void handleDeleteFail(Long id, Model model, StatusNotification deletedCategory) {
        if (deletedCategory.equals(StatusNotification.DELETE_FAIL)) {
            List<Product> productList = productService.findAllWithCorrespondingCategory(id).getProducts();
            List<Category> categories = productService.findAllCategories()
                    .getCategories()
                    .parallelStream()
                    .filter(category -> !category.getId().equals(id))
                    .collect(Collectors.toUnmodifiableList());
            model.addAttribute("filteredCategories", categories);
            model.addAttribute(PRODUCTS, productList);
        }
    }
}
