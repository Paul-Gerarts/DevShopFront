package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductListAndMinMaxPrice;
import be.syntra.devshop.DevshopFront.models.dtos.ProductsDisplayList;
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
import java.util.List;

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
        ProductListAndMinMaxPrice productListAndMinMaxPrice = applySearch(true);
        List<Product> productList = productListAndMinMaxPrice.getProducts();
        searchService.setPriceFilters(productListAndMinMaxPrice.getSearchResultMinPrice(),productListAndMinMaxPrice.getSearchResultMaxPrice());
        return setTemplateModel(model, productList);
    }

    @GetMapping("/pricelow/")
    public String searchPriceLow(@RequestParam String priceLow, Model model) {
        searchService.setPriceLow(new BigDecimal(priceLow));
        ProductListAndMinMaxPrice productListAndMinMaxPrice = applySearch(true);
        List<Product> productList = productListAndMinMaxPrice.getProducts();
        return setTemplateModel(model, productList);
    }

    @GetMapping("/pricehigh/")
    public String searchPriceHigh(@RequestParam String priceHigh, Model model) {
        searchService.setPriceHigh(new BigDecimal(priceHigh));
        ProductListAndMinMaxPrice productListAndMinMaxPrice = applySearch(true);
        List<Product> productList = productListAndMinMaxPrice.getProducts();
        return setTemplateModel(model, productList);
    }

    @GetMapping("/description/")
    public String searchProductDescription(@RequestParam String description, Model model) {
        searchService.setDescription(description);
        ProductListAndMinMaxPrice productListAndMinMaxPrice = applySearch(true);
        List<Product> productList = productListAndMinMaxPrice.getProducts();
        searchService.setPriceFilters(productListAndMinMaxPrice.getSearchResultMinPrice(),productListAndMinMaxPrice.getSearchResultMaxPrice());
        return setTemplateModel(model, productList);
    }

    @GetMapping("/sortbyname")
    public String sortByName(Model model) {
        searchService.setSortingByName();
        ProductListAndMinMaxPrice productListAndMinMaxPrice = applySearch(searchService.getSearchModel().isSearchResultView());
        List<Product> productList = productListAndMinMaxPrice.getProducts();
        return setTemplateModel(model, productList);
    }

    @GetMapping("/sortbyprice")
    public String sortByPrice(Model model) {
        searchService.setSortingByPrice();
        ProductListAndMinMaxPrice productListAndMinMaxPrice = applySearch(searchService.getSearchModel().isSearchResultView());
        List<Product> productList = productListAndMinMaxPrice.getProducts();
        return setTemplateModel(model, productList);
    }

    @GetMapping("/archived/sortbyname")
    public String sortArchivedByName(Model model) {
        searchService.setSortingByName();
        searchService.setArchivedView(true);
        List<Product> productList = productService.findAllProductsBySearchModel().getProducts();
        return setTemplateModel(model, productList);
    }

    @GetMapping("/archived/sortbyprice")
    public String sortArchivedByPrice(Model model) {
        searchService.setSortingByPrice();
        searchService.setArchivedView(true);
        List<Product> productList = productService.findAllProductsBySearchModel().getProducts();
        return setTemplateModel(model, productList);
    }

    private ProductListAndMinMaxPrice applySearch(boolean isSearchResultView) {
        searchService.setSearchResultView(isSearchResultView);
        searchService.setArchivedView(false);
        return productService.findAllProductsBySearchModel();
    }

    private String setTemplateModel(Model model, List<Product> productList) {
        ProductList productListDto = new ProductList(productList);
        ProductsDisplayList productsDisplay = productMapper.convertToProductDtoList(productListDto);
        model.addAttribute(SEARCH_MODEL, searchService.getSearchModel());
        model.addAttribute(PRODUCTS, productsDisplay);
        model.addAttribute("cart", cartService.getCart());
        return PRODUCT_OVERVIEW;
    }
}
