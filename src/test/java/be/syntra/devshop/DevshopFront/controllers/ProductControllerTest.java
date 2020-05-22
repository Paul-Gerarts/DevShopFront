package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.StarRating;
import be.syntra.devshop.DevshopFront.models.dtos.CartDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductList;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;
import be.syntra.devshop.DevshopFront.services.CartService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.StarRatingService;
import be.syntra.devshop.DevshopFront.services.utils.ProductMapper;
import be.syntra.devshop.DevshopFront.testutils.TestSecurityConfig;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import org.junit.jupiter.api.Test;
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
import static be.syntra.devshop.DevshopFront.testutils.CartUtils.getCartWithOneDummyProduct;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.*;
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
    MockMvc mockMvc;

    @MockBean
    ProductMapper productMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private CartService cartService;

    @MockBean
    private SearchService searchService;

    @MockBean
    private StarRatingService ratingService;

    @Test
    void displayProductOverViewTest() throws Exception {

        // given
        final CartDto dummyCartDto = getCartWithOneDummyProduct();
        final ProductList dummyProductList = getDummyProductList();
        SearchModel searchModelDummy = new SearchModel();
        when(cartService.getCart()).thenReturn(dummyCartDto);
        when(searchService.getSearchModel()).thenReturn(searchModelDummy);
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductList);
        when(productMapper.convertToProductsDisplayListDto(any(ProductList.class))).thenReturn(getDummyProductDtoList());

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
        verify(searchService, times(1)).getSearchModel();
        verify(cartService, times(1)).getCart();
    }

    @Test
    @WithMockUser
    void displayProductDetailsTest() throws Exception {
        // given
        final Product dummyProduct = getDummyNonArchivedProduct();
        final StarRatingDto ratingDto = createStarRatingDto();
        when(productService.findById(dummyProduct.getId())).thenReturn(dummyProduct);
        when(ratingService.findByUserNameAndId(dummyProduct.getId(), "user")).thenReturn(ratingDto);

        // when
        final ResultActions getResult = mockMvc.perform(get("/products/details/" + dummyProduct.getId()));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productDetails"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", dummyProduct));

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
        when(cartService.getCart()).thenReturn(dummyCartDto);

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

        verify(productService, times(1)).addToCart(any());
        verify(productService, times(1)).findById(dummyProduct.getId());
        verify(productService, times(1)).findAllProductsBySearchModel();
        verify(productMapper, times(1)).convertToProductsDisplayListDto(any());
        verify(searchService, times(1)).getSearchModel();
        verify(cartService, times(1)).getCart();
    }

    @Test
    @WithMockUser
    void canSubmitRatingTest() throws Exception {
        // given
        final StarRatingDto starRatingDto = createStarRatingDto();
        final Product dummyProduct = getDummyNonArchivedProduct();
        final Set<StarRating> ratings = createStarRatingSet();
        dummyProduct.setRatings(ratings);
        when(productService.findById(dummyProduct.getId())).thenReturn(dummyProduct);
        when(ratingService.findByUserNameAndId(dummyProduct.getId(), "user")).thenReturn(starRatingDto);
        when(ratingService.submitRating(dummyProduct.getId(), starRatingDto.getRating(), "user")).thenReturn(SUCCESS);
        when(cartService.getCart()).thenReturn(getCartWithOneDummyProduct());

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
                .andExpect(model().attribute("product", dummyProduct))
                .andExpect(model().attribute("status", SUCCESS))
                .andExpect(model().attribute("rating", starRatingDto));

        verify(productService, times(1)).findById(dummyProduct.getId());
        verify(ratingService, times(1)).findByUserNameAndId(dummyProduct.getId(), "user");
        verify(ratingService, times(1)).submitRating(dummyProduct.getId(), starRatingDto.getRating(), "user");
        verify(cartService, times(1)).getCart();
    }
}
