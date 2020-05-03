package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;
    private final SearchService searchService;
    private final ProductMapperUtil productMapperUtil;

    @Autowired
    public ProductController(
            ProductService productService,
            CartService cartService,
            SearchService searchService,
            ProductMapperUtil productMapperUtil
    ) {
        this.productService = productService;
        this.cartService = cartService;
        this.searchService = searchService;
        this.productMapperUtil = productMapperUtil;
    }

    @GetMapping
    public String displayProductOverview(Model model) {
        //ProductListDto productListDto = productService.findAllNonArchived();
        searchService.resetSearchModel();
        ProductListDto productListDto = productService.findAllProductsBySearchModel();
        ProductDtoList productDtoList = productMapperUtil.convertToProductDtoList(productListDto);
        //searchService.resetSearchModel();
        productService.setPriceFilters(productListDto.getProducts());
        model.addAttribute("productlist", productDtoList);
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @GetMapping("/details/{id}")
    public String handleGet(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("cart", cartService.getCart());
        return "product/productDetails";
    }

    @PostMapping("/details/{id}")
    public String archiveProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        StatusNotification statusNotification = productService.archiveProduct(product);
        model.addAttribute("product", product);
        model.addAttribute("status", statusNotification);
        return "product/productDetails";
    }

    @PostMapping
    public String addSelectedProductToCart(@ModelAttribute("id") Long id, Model model) {
        log.info("addSelectedProductToCart()-> {}", id);
        productService.addToCart(productService.findById(id));
        // todo: in backend combine these 2 if set in searchModel
        /*List<Product> products = productService.findBySearchRequest(searchService.getSearchModel()).getProducts();
        final ProductListDto productListDto = productService.filterByPrice(products, searchService.getSearchModel());*/
        ProductListDto productListDto = productService.findAllProductsBySearchModel();
        ProductDtoList productDtoList = productMapperUtil.convertToProductDtoList(productListDto);
        model.addAttribute("productlist", productDtoList);
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @PostMapping("/details/addtocart/{id}")
    public String addSelectedProductFromDetailToCart(@PathVariable Long id) {
        log.info("addSelectedProductFromDetailToCart()-> {}", id);
        productService.addToCart(productService.findById(id));
        return "redirect:/products/details/" + id;
    }
}
