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
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Slf4j
@Controller
@RequestMapping("/search")
public class SearchController {

    private final CartService cartService;
    private final SearchService searchService;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private static final String PRODUCTS = "productlist";
    private static final String SEARCH_MODEL = "searchModel";
    private static final String PRODUCT_OVERVIEW = "product/productOverview";

    @Autowired
    public SearchController(
            CartService cartService,
            SearchService searchService,
            ProductService productService,
            ProductMapper productMapper
    ) {
        this.cartService = cartService;
        this.searchService = searchService;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping("/")
    public String showSearchBarResult(@RequestParam String searchRequest, Model model) {
        searchService.setSearchRequest(searchRequest);
        return setTemplateModel(model, applySearch(true));
    }

    @GetMapping("/pricelow/")
    public String searchPriceLow(@RequestParam String priceLow, Model model) {
        searchService.setPriceLow(new BigDecimal(priceLow));
        return setTemplateModel(model, applySearch(true));
    }

    @GetMapping("/pricehigh/")
    public String searchPriceHigh(@RequestParam String priceHigh, Model model) {
        searchService.setPriceHigh(new BigDecimal(priceHigh));
        return setTemplateModel(model, applySearch(true));
    }

    @GetMapping("/description/")
    public String searchProductDescription(@RequestParam String description, Model model) {
        searchService.setDescription(description);
        return setTemplateModel(model, applySearch(true));
    }

    @GetMapping("/category/")
    public String searchProductCategory(@RequestParam String category, Model model) {
        searchService.setSelectedCategory(category);
        searchService.setRemainingCategories(category);
        return setTemplateModel(model, applySearch(true));
    }

    @GetMapping("/sortbyname")
    public String sortByName(Model model) {
        searchService.setSortingByName();
        return setTemplateModel(model, applySearch(searchService.getSearchModel().isSearchResultView()));
    }

    @GetMapping("/sortbyprice")
    public String sortByPrice(Model model) {
        searchService.setSortingByPrice();
        return setTemplateModel(model, applySearch(searchService.getSearchModel().isSearchResultView()));
    }

    @GetMapping("/archived/sortbyname")
    public String sortArchivedByName(Model model) {
        searchService.setSortingByName();
        searchService.setArchivedView(true);
        return setTemplateModel(model, productService.findAllProductsBySearchModel());
    }

    @GetMapping("/archived/sortbyprice")
    public String sortArchivedByPrice(Model model) {
        searchService.setSortingByPrice();
        searchService.setArchivedView(true);
        return setTemplateModel(model, productService.findAllProductsBySearchModel());
    }

    private ProductList applySearch(boolean isSearchResultView) {
        searchService.setSearchResultView(isSearchResultView);
        searchService.setArchivedView(false);
        return productService.findAllProductsBySearchModel();
    }

    private String setTemplateModel(Model model, ProductList productList) {
        model.addAttribute(SEARCH_MODEL, searchService.getSearchModel());
        model.addAttribute(PRODUCTS, productMapper.convertToProductsDisplayListDto(productList));
        model.addAttribute("cart", cartService.getCart());
        return PRODUCT_OVERVIEW;
    }
}
