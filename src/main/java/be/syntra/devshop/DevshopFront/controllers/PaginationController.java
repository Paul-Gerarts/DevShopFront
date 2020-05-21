package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/pagination")
public class PaginationController {
    private final SearchService searchService;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final CartService cartService;

    private static final String PRODUCTS = "productlist";
    private static final String SEARCH_MODEL = "searchModel";
    private static final String PRODUCT_OVERVIEW = "product/productOverview";

    @Autowired
    public PaginationController(
            SearchService searchService,
            ProductService productService,
            ProductMapper productMapper,
            CartService cartService
    ) {
        this.searchService = searchService;
        this.productService = productService;
        this.productMapper = productMapper;
        this.cartService = cartService;
    }

    @GetMapping("/previous")
    public String requestPreviousPage(Model model) {
        log.info("previous page requested");
        return setTemplateModel(model, applySearch());
    }

    @GetMapping("/next")
    public String requestNextPage(Model model) {
        log.info("next page requested");
        return setTemplateModel(model, applySearch());
    }

    private String setTemplateModel(Model model, ProductList productList) {
        model.addAttribute(SEARCH_MODEL, searchService.getSearchModel());
        model.addAttribute(PRODUCTS, productMapper.convertToProductsDisplayListDto(productList));
        model.addAttribute("cart", cartService.getCart());
        return PRODUCT_OVERVIEW;
    }

    private ProductList applySearch() {
        return productService.findAllProductsBySearchModel();
    }
}
