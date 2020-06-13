package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pagination")
public class PaginationController {
    @Value("${pagination.page.size}")
    private int[] pageSizes;

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
        searchService.requestPreviousPage();
        return setTemplateModel(model, applySearch());
    }

    @GetMapping("/next")
    public String requestNextPage(Model model) {
        searchService.requestNextPage();
        return setTemplateModel(model, applySearch());
    }

    @GetMapping("/first")
    public String requestFirstPage(Model model) {
        searchService.requestFirstPage();
        return setTemplateModel(model, applySearch());
    }

    @GetMapping("/last/{pageNumber}")
    public String requestLastPage(@PathVariable Integer pageNumber, Model model) {
        searchService.requestLastPage(pageNumber);
        return setTemplateModel(model, applySearch());
    }

    @PostMapping("/size")
    public String changePageSize(@RequestParam int pageSize, Model model) {
        searchService.getSearchModel().setPageNumber(0);
        searchService.getSearchModel().setPageSize(pageSize);
        return setTemplateModel(model, applySearch());
    }

    private String setTemplateModel(Model model, ProductList productList) {
        model.addAttribute("pageSizeList", pageSizes);
        model.addAttribute("selectedPageSize", searchService.getSearchModel().getPageSize());
        model.addAttribute(SEARCH_MODEL, searchService.getSearchModel());
        model.addAttribute(PRODUCTS, productMapper.convertToProductsDisplayListDto(productList));
        //model.addAttribute("cart", cartService.getCart());
        model.addAttribute("cart", cartService.getCartDisplayDto());
        return PRODUCT_OVERVIEW;
    }

    private ProductList applySearch() {
        return productService.findAllProductsBySearchModel();
    }
}
