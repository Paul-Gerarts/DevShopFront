package be.syntra.devshop.DevshopFront.controllers;

import be.syntra.devshop.DevshopFront.configuration.WebConfig;
import be.syntra.devshop.DevshopFront.exceptions.JWTTokenExceptionHandler;
import be.syntra.devshop.DevshopFront.models.*;
import be.syntra.devshop.DevshopFront.models.dtos.CategoryList;
import be.syntra.devshop.DevshopFront.models.dtos.ProductDto;
import be.syntra.devshop.DevshopFront.models.dtos.ProductListAndMinMaxPrice;
import be.syntra.devshop.DevshopFront.models.dtos.ProductsDisplayList;
import be.syntra.devshop.DevshopFront.services.CategoryService;
import be.syntra.devshop.DevshopFront.services.ProductService;
import be.syntra.devshop.DevshopFront.services.SearchService;
import be.syntra.devshop.DevshopFront.services.utils.CategoryMapper;
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

import java.util.Arrays;
import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.CategoryUtils.createCategoryList;
import static be.syntra.devshop.DevshopFront.testutils.ProductUtils.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {WebConfig.class, JWTTokenExceptionHandler.class})
)
@ContextConfiguration(classes = {TestWebConfig.class, TestSecurityConfig.class})
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private SearchService searchService;

    @MockBean
    private ProductMapper productMapper;

    @MockBean
    private CategoryMapper categoryMapper;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayAddProductsFrom() throws Exception {

        //given
        List<Category> categories = createCategoryList();
        when(productService.createEmptyProduct()).thenReturn(new ProductDto());
        when(productService.findAllCategories()).thenReturn(new CategoryList(categories));

        //when
        final ResultActions getResult = mockMvc.perform(get("/admin/addproduct"));

        //then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addProduct"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Add Product")))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", new ProductDto()));

        verify(productService, times(1)).createEmptyProduct();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getProductEntry() throws Exception {

        // given
        ProductDto dummyProductDto = getDummyProductDto();
        List<Category> categories = createCategoryList();
        String[] categoryNames = {"Headphones"};
        when(productService.findAllCategories()).thenReturn(new CategoryList(categories));
        when(productService.addProduct(dummyProductDto)).thenReturn(StatusNotification.SAVED);

        // when
        final ResultActions postRestult = mockMvc.perform(
                post("/admin/addproduct")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("id", String.valueOf(1))
                        .param("name", "product")
                        .param("price", "88")
                        .param("description", "description")
                        .param("archived", String.valueOf(false))
                        .param("categoryNames", categoryNames));

        // then
        postRestult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addProduct"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Add Product")))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", dummyProductDto));

        verify(productService, times(1)).addProduct(dummyProductDto);
        verify(productService, times(0)).createEmptyProduct();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayAdminFunctionsPageTest() throws Exception {
        //given

        //when
        final ResultActions getResult = mockMvc.perform(get("/admin/overview"));

        //then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/adminOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("What would you like to do?")))
                .andExpect(model().attributeExists("functions"))
                .andExpect(model().attribute("functions", Arrays.asList(AdminFunctions.values())));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayAddProductFormWhenEditingTest() throws Exception {
        //given
        Product dummyProduct = getDummyNonArchivedProduct();
        ProductDto dummyProductDto = getDummyProductDto();
        List<Category> categories = createCategoryList();
        List<String> categoryNames = List.of("Headphones");
        when(categoryMapper.mapToCategoryNames(categories)).thenReturn(categoryNames);
        when(productMapper.convertToProductDto(dummyProduct)).thenReturn(dummyProductDto);
        when(productService.findById(dummyProduct.getId())).thenReturn(dummyProduct);
        when(productService.findAllCategories()).thenReturn(new CategoryList(categories));

        //when
        final ResultActions getResult = mockMvc.perform(get("/admin/product/" + dummyProduct.getId() + "/edit"));

        //then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addProduct"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Update Product")))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", dummyProductDto));

        verify(productService, times(1)).findById(dummyProduct.getId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getUpdatedProductTest() throws Exception {
        // given
        ProductDto dummyProductDto = getDummyProductDto();
        List<Category> categories = createCategoryList();
        String[] categoryNames = {"Headphones"};
        when(productService.findAllCategories()).thenReturn(new CategoryList(categories));
        when(productService.addProduct(dummyProductDto)).thenReturn(StatusNotification.SAVED);

        // when
        final ResultActions postRestult = mockMvc.perform(
                post("/admin/product/" + dummyProductDto.getId() + "/edit")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("name", "product")
                        .param("price", "88")
                        .param("description", "description")
                        .param("archived", String.valueOf(false))
                        .param("categoryNames", categoryNames));

        // then
        postRestult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addProduct"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(containsString("Update Product")))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", dummyProductDto));

        verify(productService, times(1)).addProduct(dummyProductDto);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayProductArchivedOverViewTest() throws Exception {
        // given
        final ProductListAndMinMaxPrice dummyProductListAndMinMaxPrice = getDummyProductListAndMinMaxPrice();
        final ProductsDisplayList productsDisplayList = getDummyProductDtoList();
        SearchModel searchModelDummy = new SearchModel();
        when(searchService.getSearchModel()).thenReturn(searchModelDummy);
        when(productService.findAllProductsBySearchModel()).thenReturn(dummyProductListAndMinMaxPrice);
        when(productMapper.convertToProductDtoList(any())).thenReturn(productsDisplayList);

        // when
        final ResultActions getResult = mockMvc.perform(get("/admin/archived"));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("product/productOverview"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("productlist"))
                .andExpect(model().attribute("productlist", productsDisplayList));

        verify(searchService, times(1)).resetSearchModel();
        verify(searchService, times(1)).setSearchResultView(false);
        verify(searchService, times(1)).setArchivedView(true);
        verify(searchService, times(1)).getSearchModel();
        verify(productService, times(1)).findAllProductsBySearchModel();
        verify(productMapper, times(1)).convertToProductDtoList(any());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayManageCategoryViewTest() throws Exception {
        // given
        final List<Category> categories = createCategoryList();
        final CategoryList categoryList = new CategoryList(categories);
        when(productService.findAllCategories()).thenReturn(categoryList);

        // when
        final ResultActions getResult = mockMvc.perform(get("/admin/manage_category"));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addCategory"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("categories", categories));

        verify(productService, times(1)).findAllCategories();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayManageCategoryViewAfterDeleteTest() throws Exception {
        // given
        final List<Category> categories = createCategoryList();
        final CategoryList categoryList = new CategoryList(categories);
        final Long categoryToDelete = categories.get(0).getId();
        when(productService.findAllCategories()).thenReturn(categoryList);
        when(categoryService.delete(categoryToDelete)).thenReturn(StatusNotification.DELETED);

        // when
        final ResultActions getResult = mockMvc.perform(post("/admin/manage_category/delete/" + categoryToDelete)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("id", String.valueOf(categoryToDelete)));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addCategory"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("categories", "status"))
                .andExpect(model().attribute("categories", categories))
                .andExpect(model().attribute("status", StatusNotification.DELETED));

        verify(productService, times(1)).findAllCategories();
        verify(categoryService, times(1)).delete(categoryToDelete);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayManageCategoryViewAfterUpdateTest() throws Exception {
        // given
        final List<Category> categories = createCategoryList();
        final CategoryList categoryList = new CategoryList(categories);
        final Long categoryToUpdate = categories.get(0).getId();
        final String newCategoryName = "Test";
        when(productService.findAllCategories()).thenReturn(categoryList);
        when(categoryService.updateCategory(newCategoryName, categoryToUpdate)).thenReturn(StatusNotification.SUCCESS);

        // when
        final ResultActions getResult = mockMvc.perform(post("/admin/manage_category/update_category/" + newCategoryName + "/" + categoryToUpdate)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("categoryToUpdate", newCategoryName)
                .param("categoryToSet", String.valueOf(categoryToUpdate)));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addCategory"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("categories", "status"))
                .andExpect(model().attribute("categories", categories))
                .andExpect(model().attribute("status", StatusNotification.SUCCESS));

        verify(productService, times(1)).findAllCategories();
        verify(categoryService, times(1)).updateCategory(newCategoryName, categoryToUpdate);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayManageCategoryViewAfterCreatingNewCategoryTest() throws Exception {
        // given
        final List<Category> categories = createCategoryList();
        final CategoryList categoryList = new CategoryList(categories);
        final Long categoryToCreate = 0L;
        final String newCategoryName = "Test";
        when(productService.findAllCategories()).thenReturn(categoryList);
        when(categoryService.updateCategory(newCategoryName, categoryToCreate)).thenReturn(StatusNotification.SUCCESS);

        // when
        final ResultActions getResult = mockMvc.perform(post("/admin/manage_category/new")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("newCategoryName", newCategoryName)
                .param("categoryToSet", String.valueOf(categoryToCreate)));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addCategory"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("categories", "status"))
                .andExpect(model().attribute("categories", categories))
                .andExpect(model().attribute("status", StatusNotification.SUCCESS));

        verify(productService, times(3)).findAllCategories();
        verify(categoryService, times(1)).updateCategory(newCategoryName, categoryToCreate);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void displayManageCategoryViewAfterDeleteConfirmationTest() throws Exception {
        // given
        final List<Category> categories = createCategoryList();
        final CategoryList categoryList = new CategoryList(categories);
        final Long categoryToDelete = categories.get(0).getId();
        final long categoryToSet = 2L;
        when(productService.findAllCategories()).thenReturn(categoryList);
        when(categoryService.setNewCategories(categoryToDelete, categoryToSet)).thenReturn(StatusNotification.SUCCESS);
        when(categoryService.delete(categoryToDelete)).thenReturn(StatusNotification.DELETED);

        // when
        final ResultActions getResult = mockMvc.perform(post("/admin/manage_category/set_category/" + categoryToDelete + "/" + categoryToSet)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("categoryToDelete", String.valueOf(categoryToDelete))
                .param("categoryToSet", String.valueOf(categoryToSet)));

        // then
        getResult
                .andExpect(status().isOk())
                .andExpect(view().name("admin/product/addCategory"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("categories", "status"))
                .andExpect(model().attribute("categories", categories))
                .andExpect(model().attribute("status", StatusNotification.DELETED));

        verify(productService, times(1)).findAllCategories();
        verify(categoryService, times(1)).setNewCategories(categoryToDelete, categoryToSet);
        verify(categoryService, times(1)).delete(categoryToDelete);
    }
}