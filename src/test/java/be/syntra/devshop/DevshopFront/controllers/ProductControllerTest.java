package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.*;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;
import be.syntra.devshop.DevshopFront.services.*;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import be.syntra.devshop.DevshopFront.testutils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;

import static be.syntra.devshop.DevshopFront.models.StatusNotification.SUCCESS;
import static be.syntra.devshop.DevshopFront.models.StatusNotification.UPDATED;
import static be.syntra.devshop.DevshopFront.testutils.CartUtils.getCartProductsDisplayDto;
import static be.syntra.devshop.DevshopFront.testutils.CartUtils.getCartWithOneDummyProduct;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.*;
import static be.syntra.devshop.DevshopFront.testutils.ReviewUtils.getDummyReview;
import static be.syntra.devshop.DevshopFront.testutils.StarRatingUtils.createStarRatingDto;
import static be.syntra.devshop.DevshopFront.testutils.StarRatingUtils.createStarRatingSet;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private CartService cartService;

    @MockBean
    private SearchService searchService;

    @MockBean
    private StarRatingService ratingService;

    @MockBean
    private ReviewService reviewService;

    @Test
    void displayProductOverViewTest() throws Exception {

        // given
        final ProductList dummyProductList = getDummyProductList();
        SearchModel searchModelDummy = new SearchModel();
        when(searchService.getSearchModel()).thenReturn(searchModelDummy);
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductList);
        when(productMapper.convertToProductsDisplayListDto(any(ProductList.class))).thenReturn(getDummyProductDtoList());
        when(cartService.getCartDisplayDto()).thenReturn(getCartProductsDisplayDto());

        // when
        final ResultActions getResult = mockMvc.perform(get("/products"));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("productlist", "searchModel", "cart"));


        verify(productService, times(1)).findAllProductsBySearchModel();
        verify(productMapper, times(1)).convertToProductsDisplayListDto(any());
        verify(searchService, times(2)).getSearchModel();
        verify(cartService, times(1)).getCartDisplayDto();
    }

    @Test
    @WithMockUser
    void displayProductDetailsTest() throws Exception {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();
        final StarRatingDto ratingDto = createStarRatingDto();
        final ProductDto dummyProductDto = getDummyProductDto();
        when(productService.findById(dummyProduct.getId())).thenReturn(dummyProduct);
        when(ratingService.findByUserNameAndId(dummyProduct.getId(), "user")).thenReturn(ratingDto);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);
        when(reviewService.findByUserNameAndId(anyLong(), anyString())).thenReturn(getDummyReview());
        when(cartService.getCartDisplayDto()).thenReturn(getCartProductsDisplayDto());

        // when
        final ResultActions getResult = mockMvc.perform(get("/products/details/" + dummyProduct.getId()));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productDetails"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", dummyProductDto));


        verify(productService, times(1)).findById(dummyProduct.getId());
        verify(ratingService, times(1)).findByUserNameAndId(dummyProduct.getId(), "user");
    }

    @Test
    void canArchiveProductTest() throws Exception {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();
        final ProductDto productDtoDummy = getDummyProductDto();
        when(productService.findById(dummyProduct.getId())).thenReturn(dummyProduct);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(productDtoDummy);
        when(productService.archiveProduct(productDtoDummy)).thenReturn(UPDATED);

        // when
        final ResultActions getResult = mockMvc.perform(post("/products/details/" + dummyProduct.getId()));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productDetails"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("product", "status"))
                .andExpect(model().attribute("product", dummyProduct))
                .andExpect(model().attribute("status", UPDATED));

        verify(productService, times(1)).findById(dummyProduct.getId());
        verify(productService, times(1)).archiveProduct(productDtoDummy);
    }

    @Test
    void addSelectedProductToCart() throws Exception {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();
        final ProductList dummyProductList = getDummyProductList();
        final CartDto dummyCartDto = getCartWithOneDummyProduct();
        SearchModel searchModelDummy = new SearchModel();
        when(searchService.getSearchModel()).thenReturn(searchModelDummy);
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductList);
        when(productMapper.convertToProductsDisplayListDto(any(ProductList.class))).thenReturn(getDummyProductDtoList());
        when(cartService.getCartDisplayDto()).thenReturn(getCartProductsDisplayDto());
        doNothing().when(cartService).addToCart(dummyProduct.getId());

        // when
        final ResultActions getResult = mockMvc.perform(post("/products/")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("id", dummyProduct.getId().toString())
                .param("name", dummyProduct.getName())
                .param("price", dummyProduct.getPrice().toString()));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("productlist", "searchModel", "cart"));

        verify(productService, times(1)).findAllProductsBySearchModel();
        verify(productMapper, times(1)).convertToProductsDisplayListDto(any());
        verify(searchService, times(2)).getSearchModel();
        verify(cartService, times(1)).getCartDisplayDto();
        verify(cartService, times(1)).addToCart(dummyProduct.getId());
    }

    @Test
    @WithMockUser
    void canSubmitRatingTest() throws Exception {
        // given
        final StarRatingDto starRatingDto = createStarRatingDto();
        final Product dummyProduct = getDummyNonArchivedProduct();
        final Set<StarRating> ratings = createStarRatingSet();
        final ProductDto dummyProductDto = getDummyProductDto();
        dummyProduct.setRatings(ratings);
        when(productService.findById(dummyProduct.getId())).thenReturn(dummyProduct);
        when(ratingService.findByUserNameAndId(dummyProduct.getId(), "user")).thenReturn(starRatingDto);
        when(ratingService.submitRating(dummyProduct.getId(), starRatingDto.getRating(), "user")).thenReturn(SUCCESS);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);
        when(reviewService.findByUserNameAndId(anyLong(), anyString())).thenReturn(getDummyReview());
        when(cartService.getCartDisplayDto()).thenReturn(getCartProductsDisplayDto());

        // when
        final ResultActions getResult = mockMvc.perform(post("/products/"
                + dummyProduct.getId()
                + "/ratings/"
                + starRatingDto.getRating()));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productDetails"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("product", "status", "rating"))
                .andExpect(model().attribute("product", dummyProductDto))
                .andExpect(model().attribute("status", SUCCESS))
                .andExpect(model().attribute("rating", starRatingDto));

        verify(productService, times(1)).findById(dummyProduct.getId());
        verify(ratingService, times(1)).findByUserNameAndId(dummyProduct.getId(), "user");
        verify(ratingService, times(1)).submitRating(dummyProduct.getId(), starRatingDto.getRating(), "user");
        verify(cartService, times(1)).getCartDisplayDto();
    }

    @Test
    @WithMockUser
    void addSelectedProductToCartFromDetailsPageTest() throws Exception {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();
        doNothing().when(cartService).addToCart(dummyProduct.getId());

        // when
        final ResultActions getResult = mockMvc.perform(post("/products/details/addtocart/" + dummyProduct.getId()));

        // then
        getResult
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/products/details/" + dummyProduct.getId()));

        verify(cartService, times(1)).addToCart(dummyProduct.getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/delete_review", "/update_review", "/add_review"})
    @WithMockUser
    void removeAddUpdateReviewTest(String endPoint) throws Exception {
        // given
        final Product dummyProduct = getDummyProductWithReview();
        final Review dummyReview = getDummyReview();
        final StarRatingDto starRatingDto = createStarRatingDto();
        final ProductDto dummyProductDto = getDummyProductDtoWithReview();
        final CartDto dummyCartDto = getCartWithOneDummyProduct();
        when(reviewService.submitReview(anyLong(), any())).thenReturn(StatusNotification.SAVED);
        when(reviewService.updateReview(anyLong(), any())).thenReturn(StatusNotification.REVIEW_UPDATED);
        when(reviewService.removeReview(anyLong(), any())).thenReturn(StatusNotification.REVIEW_DELETED);
        when(productService.findById(any())).thenReturn(dummyProduct);
        when(ratingService.findByUserNameAndId(anyLong(), anyString())).thenReturn(starRatingDto);
        when(reviewService.findByUserNameAndId(anyLong(), anyString())).thenReturn(dummyReview);
        when(productMapper.convertToProductDto(any())).thenReturn(dummyProductDto);
        //when(cartService.getCart()).thenReturn(dummyCartDto);
        when(cartService.getCartDisplayDto()).thenReturn(getCartProductsDisplayDto());

        // when
        final ResultActions getResult = mockMvc.perform(post("/products/details/" + dummyProduct.getId() + endPoint)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("userName", dummyReview.getUserName())
                .param("reviewText", dummyReview.getReviewText()));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productDetails"))
                .andExpect(model().attributeExists("product", "cart", "rating", "status"));
    }
}
