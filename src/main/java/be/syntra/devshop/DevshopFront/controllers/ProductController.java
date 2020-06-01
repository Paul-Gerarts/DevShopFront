package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;
import be.syntra.devshop.DevshopFront.services.*;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/products")
public class ProductController {
    @Value("${pagination.page.size}")
    private int[] pageSizes;
    private static final String REDIRECT_PRODUCT_DETAILS = "redirect:/products/details/";


    private final ProductService productService;
    private final CartService cartService;
    private final SearchService searchService;
    private final StarRatingService starRatingService;
    private final ProductMapper productMapper;
    private final ReviewService reviewService;

    @Autowired
    public ProductController(
            ProductService productService,
            CartService cartService,
            SearchService searchService,
            StarRatingService starRatingService,
            ProductMapper productMapper,
            ReviewService reviewService
    ) {
        this.productService = productService;
        this.cartService = cartService;
        this.searchService = searchService;
        this.starRatingService = starRatingService;
        this.productMapper = productMapper;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String displayProductOverview(Model model) {
        searchService.resetSearchModel();
        productService.addSelectableCategoriesToSearchModel();
        ProductList productList = productService.findAllProductsBySearchModel();
        searchService.setPriceFilters(BigDecimal.ZERO, productList.getSearchResultMaxPrice());
        model.addAttribute("pageSizeList", pageSizes);
        model.addAttribute("selectedPageSize", searchService.getSearchModel().getPageSize());
        model.addAttribute("productlist", productMapper.convertToProductsDisplayListDto(productList));
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @GetMapping("/details/{id}")
    public String getProductDetailsById(@PathVariable Long id, Model model, Principal user) {
        return getProductDetails(id, model, user);
    }

    @PostMapping("/details/{id}")
    public String archiveProduct(@PathVariable Long id, Model model, Principal user) {
        Product product = productService.findById(id);
        product.setArchived(true);
        StatusNotification statusNotification = productService.archiveProduct(productMapper.convertToProductDto(product));
        StarRatingDto rating = starRatingService.findByUserNameAndId(id, nullSafe(user));
        model.addAttribute("rating", rating);
        model.addAttribute("product", product);
        model.addAttribute("status", statusNotification);
        return "product/productDetails";
    }

    @PostMapping("{productId}/ratings/{count}")
    public String submitRating(@PathVariable Long productId, @PathVariable Double count, Model model, Principal user) {
        StatusNotification status = starRatingService.submitRating(productId, count, nullSafe(user));
        model.addAttribute("status", status);
        return getProductDetails(productId, model, user);
    }

    @PostMapping
    public String addSelectedProductToCart(@ModelAttribute("id") Long id, Model model) {
        productService.addToCart(productService.findById(id));
        ProductList productList = productService.findAllProductsBySearchModel();
        model.addAttribute("pageSizeList", pageSizes);
        model.addAttribute("selectedPageSize", searchService.getSearchModel().getPageSize());
        model.addAttribute("productlist", productMapper.convertToProductsDisplayListDto(productList));
        model.addAttribute("searchModel", searchService.getSearchModel());
        model.addAttribute("cart", cartService.getCart());
        return "product/productOverview";
    }

    @PostMapping("/details/addtocart/{id}")
    public String addSelectedProductFromDetailToCart(@PathVariable Long id) {
        productService.addToCart(productService.findById(id));
        return REDIRECT_PRODUCT_DETAILS + id;
    }

    @PostMapping("/details/{id}/add_review")
    public String addReviewToProduct(@Valid @PathVariable Long id, @ModelAttribute("review") Review review, Model model, Principal user) {
        final StatusNotification statusNotification = reviewService.submitReview(id, review);
        return getProductDetails(id, model, user, statusNotification);
    }

    @PostMapping("/details/{id}/update_review")
    public String updateReviewForProduct(@Valid @PathVariable Long id, @ModelAttribute("review") Review review, Model model, Principal user) {
        final StatusNotification statusNotification = reviewService.updateReview(id, review);
        return getProductDetails(id, model, user, statusNotification);
    }

    @PostMapping("/details/{id}/delete_review")
    public String removeReviewFromProduct(@PathVariable Long id, @ModelAttribute("review") Review review, Model model, Principal user) {
        final StatusNotification statusNotification = reviewService.removeReview(id, review);
        return getProductDetails(id, model, user, statusNotification);
    }

    private String getProductDetails(Long id, Model model, Principal user) {
        Product product = productService.findById(id);
        StarRatingDto rating = starRatingService.findByUserNameAndId(id, nullSafe(user));
        if (null != user) {
            model.addAttribute("review", reviewService.findByUserNameAndId(id, user.getName()));
        }
        model.addAttribute("product", productMapper.convertToDisplayProductDto(product));
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("rating", rating);
        return "product/productDetails";
    }

    private String getProductDetails(Long id, Model model, Principal user, StatusNotification statusNotification) {
        Product product = productService.findById(id);
        StarRatingDto rating = starRatingService.findByUserNameAndId(id, nullSafe(user));
        if (null != user) {
            model.addAttribute("review", reviewService.findByUserNameAndId(id, user.getName()));
        }
        model.addAttribute("product", productMapper.convertToDisplayProductDto(product));
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("rating", rating);
        model.addAttribute("status", statusNotification);
        return "product/productDetails";
    }

    private String nullSafe(Principal user) {
        return null == user
                ? ""
                : user.getName();
    }
}
