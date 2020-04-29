package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDtoList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductListDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapperUtil;
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
    private final ProductMapperUtil productMapperUtil;
    private static final String PRODUCTS = "productlist";
    private static final String SEARCH_MODEL = "searchModel";
    private static final String PRODUCT_OVERVIEW = "product/productOverview";

    @Autowired
    public SearchController(
            CartService cartService,
            SearchService searchService,
            ProductService productService,
            ProductMapperUtil productMapperUtil
    ) {
        this.cartService = cartService;
        this.searchService = searchService;
        this.productService = productService;
        this.productMapperUtil = productMapperUtil;
    }

    @GetMapping("/")
    public String showSearchBarResult(@RequestParam String searchRequest, Model model) {
        searchService.setSearchRequest(searchRequest);
        List<Product> productList = applySearch(true);
        return setTemplateModel(model, productList);
    }

    @GetMapping("/pricelow/")
    public String searchPriceLow(@RequestParam String priceLow, Model model) {
        searchService.setPriceLow(new BigDecimal(priceLow));
        List<Product> productList = applySearch(true);
        return setTemplateModel(model, productList);
    }

    @GetMapping("/pricehigh/")
    public String searchPriceHigh(@RequestParam String priceHigh, Model model) {
        searchService.setPriceHigh(new BigDecimal(priceHigh));
        List<Product> productList = applySearch(true);
        return setTemplateModel(model, productList);
    }

    @GetMapping("/description/")
    public String searchProductDescription(@RequestParam String description, Model model) {
        searchService.setDescription(description);
        List<Product> productList = applySearch(true);
        return setTemplateModel(model, productList);
    }

    @GetMapping("/sortbyname")
    public String sortByName(Model model) {
        List<Product> productList = applySearch(searchService.getSearchModel().isSearchResultView());
        return getProductsSortedByName(model, productList);
    }

    @GetMapping("/sortbyprice")
    public String sortByPrice(Model model) {
        List<Product> productList = applySearch(searchService.getSearchModel().isSearchResultView());
        return getProductsSortedByPrice(model, productList);
    }

    @GetMapping("/archived/sortbyname")
    public String sortArchivedByName(Model model) {
        List<Product> productList = productService.findAllArchived().getProducts();
        return getProductsSortedByName(model, productList);
    }

    @GetMapping("/archived/sortbyprice")
    public String sortArchivedByPrice(Model model) {
        List<Product> productList = productService.findAllArchived().getProducts();
        return getProductsSortedByPrice(model, productList);
    }

    private List<Product> applySearch(boolean isSearchResultView) {
        searchService.setSearchResultView(isSearchResultView);
        searchService.setArchivedView(false);
        List<Product> productList = productService.findBySearchRequest(searchService.getSearchModel()).getProducts();
        List<Product> filteredList = productService.filterByPrice(productList, searchService.getSearchModel()).getProducts();
        return hasDescription()
                ? productService.searchForProductDescription(filteredList, searchService.getSearchModel()).getProducts()
                : filteredList;
    }

    private boolean hasDescription() {
        return null != searchService.getSearchModel().getDescription();
    }

    private String getProductsSortedByName(Model model, List<Product> productList) {
        List<Product> sortedProducts = productService.sortListByName(productList, searchService.getSearchModel()).getProducts();
        reverseNameSort();
        return setTemplateModel(model, sortedProducts);
    }

    private String getProductsSortedByPrice(Model model, List<Product> productList) {
        List<Product> sortedProducts = productService.sortListByPrice(productList, searchService.getSearchModel()).getProducts();
        reversePriceSort();
        return setTemplateModel(model, sortedProducts);
    }

    private void reverseNameSort() {
        boolean sortAscending = searchService.getSearchModel().isSortAscendingName();
        searchService.getSearchModel().setSortAscendingName(!sortAscending);
    }

    private void reversePriceSort() {
        boolean sortAscending = searchService.getSearchModel().isSortAscendingPrice();
        searchService.getSearchModel().setSortAscendingPrice(!sortAscending);
    }

    private String setTemplateModel(Model model, List<Product> productList) {
        ProductListDto productListDto = new ProductListDto(productList);
        ProductDtoList productDtoListDto = productMapperUtil.convertToProductDtoListDto(productListDto);
        model.addAttribute(SEARCH_MODEL, searchService.getSearchModel());
        model.addAttribute(PRODUCTS, productDtoListDto);
        model.addAttribute("cart", cartService.getCart());
        return PRODUCT_OVERVIEW;
    }

}
