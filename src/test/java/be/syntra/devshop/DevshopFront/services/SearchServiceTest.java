package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Category;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;
import be.syntra.devshop.DevshopFront.testutils.JsonUtils;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import be.syntra.devshop.DevshopFront.testutils.WebContextTestExecutionListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import java.math.BigDecimal;
import java.util.List;

import static be.syntra.devshop.DevshopFront.testutils.CategoryUtils.createCategoryList;
import static be.syntra.devshop.DevshopFront.testutils.StarRatingUtils.createStarRatingDto;
import static junit.framework.TestCase.*;
import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(SearchServiceImpl.class)
@Import({TestWebConfig.class, JsonUtils.class})
@TestExecutionListeners({WebContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
class SearchServiceTest {

    @Autowired
    private SearchServiceImpl searchService;

    @Test
    void canSetSearchRequestTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();

        // when
        searchModelDummy.setSearchRequest("test");
        String result = searchService.getSearchModel().getSearchRequest();

        // then
        assertThat(result).isEqualTo("test");
    }

    @Test
    void canSetArchivedViewTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        boolean isArchived = true;

        // when
        searchModelDummy.setArchivedView(isArchived);
        boolean result = searchService.getSearchModel().isArchivedView();

        // then
        assertThat(result).isEqualTo(isArchived);
    }

    @Test
    void canSetSearchResultViewTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        boolean isSearchResult = true;

        // when
        searchModelDummy.setSearchResultView(isSearchResult);
        boolean result = searchService.getSearchModel().isSearchResultView();

        // then
        assertThat(result).isEqualTo(isSearchResult);
    }

    @Test
    void canSetSearchFailureTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        boolean isSearchFailure = true;

        // when
        searchModelDummy.setSearchFailure(isSearchFailure);
        boolean result = searchService.getSearchModel().isSearchFailure();

        // then
        assertThat(result).isEqualTo(isSearchFailure);
    }

    @Test
    void canSetPriceLowTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        BigDecimal priceLow = new BigDecimal("1");

        // when
        searchModelDummy.setPriceLow(priceLow);
        BigDecimal result = searchService.getSearchModel().getPriceLow();

        // then
        assertThat(result).isEqualTo(priceLow);
    }

    @Test
    void canSetPriceHighTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        BigDecimal priceHigh = new BigDecimal("9999");

        // when
        searchModelDummy.setPriceHigh(priceHigh);
        BigDecimal result = searchService.getSearchModel().getPriceHigh();

        // then
        assertThat(result).isEqualTo(priceHigh);
    }

    @Test
    void canSetDescriptionTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        String description = "chair";

        // when
        searchModelDummy.setDescription(description);
        String result = searchService.getSearchModel().getDescription();

        // then
        assertThat(result).isEqualTo(description);
    }

    @Test
    void canResetSearchModelTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        searchModelDummy.setSearchFailure(true);
        searchModelDummy.setSearchRequest("product");
        searchModelDummy.setSearchResultView(true);
        searchModelDummy.setArchivedView(true);
        searchModelDummy.setDescription("test");
        searchModelDummy.setPriceHigh(new BigDecimal("1000"));
        searchModelDummy.setPriceLow(new BigDecimal("1"));
        searchModelDummy.setSortAscendingName(true);
        searchModelDummy.setSortAscendingPrice(true);

        // when
        searchService.resetSearchModel();

        // then
        assertThat(searchService.getSearchModel().toString()).isNotEqualTo(searchModelDummy.toString());
    }

    @Test
    void canGetSearchModelTest() {
        // when
        SearchModel searchModel = searchService.getSearchModel();

        // then
        assertThat(searchModel).isNotNull();
        assertEquals(searchModel.getClass(), SearchModel.class);
    }

    @Test
    void canSetAppliedFiltersToSearchModelTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        searchModelDummy.setSearchRequest("testRequest");

        // when
        searchService.setAppliedFiltersToSearchModel();

        // then
        assertEquals(searchModelDummy.getSearchRequest(), "testRequest");
        assertEquals(searchModelDummy.getAppliedFiltersHeader(), " with the applied filters");
        assertFalse(searchModelDummy.isSearchFailure());
        assertTrue(searchModelDummy.isActiveFilters());

    }

    @Test
    void canSetPriceFiltersTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();

        // when
        searchService.setPriceFilters(BigDecimal.ZERO, BigDecimal.TEN);

        // then
        assertEquals(searchModelDummy.getPriceLow(), BigDecimal.ZERO);
        assertEquals(searchModelDummy.getPriceHigh(), BigDecimal.TEN);
    }

    @Test
    void canSetSortingByNameTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        searchModelDummy.setNameSortActive(true);
        searchModelDummy.setPriceSortActive(true);
        searchModelDummy.setSortAscendingName(true);

        // when
        searchService.setSortingByName();

        // then
        assertTrue(searchModelDummy.isNameSortActive());
        assertFalse(searchModelDummy.isPriceSortActive());
        assertFalse(searchModelDummy.isSortAscendingName());
    }

    @Test
    void canSetSortingByPriceTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        searchModelDummy.setPriceSortActive(true);
        searchModelDummy.setNameSortActive(true);
        searchModelDummy.setSortAscendingPrice(true);

        // when
        searchService.setSortingByPrice();

        // then
        assertFalse(searchModelDummy.isNameSortActive());
        assertTrue(searchModelDummy.isPriceSortActive());
        assertFalse(searchModelDummy.isSortAscendingPrice());
    }

    @Test
    void canRequestPreviousPage() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        searchModelDummy.setPageNumber(2);

        // when
        searchService.requestPreviousPage();

        // then
        assertThat(searchModelDummy.getPageNumber().intValue()).isEqualTo(1);
    }

    @Test
    void canRequestNextPage() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        searchModelDummy.setPageNumber(2);

        // when
        searchService.requestNextPage();

        // then
        assertThat(searchModelDummy.getPageNumber().intValue()).isEqualTo(3);
    }

    @Test
    void canRequestFirstPage() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();

        // when
        searchService.requestFirstPage();

        // then
        assertThat(searchModelDummy.getPageNumber().intValue()).isEqualTo(0);
    }

    @Test
    void canRequestLastPage() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();

        // when
        searchService.requestLastPage(5);

        // then
        assertThat(searchModelDummy.getPageNumber().intValue()).isEqualTo(5);
    }

    @Test
    void canSetSelectedCategoriesTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        List<String> selectedCategory = List.of("Headphones");

        // when
        searchModelDummy.setSelectedCategories(selectedCategory);
        List<String> result = searchService.getSearchModel().getSelectedCategories();

        // then
        assertThat(result.size()).isEqualTo(selectedCategory.size());
        assertThat(result.get(0)).isEqualTo(selectedCategory.get(0));
    }

    @Test
    void canSetRemainingCategoriesTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        List<Category> categories = createCategoryList();

        // when
        searchModelDummy.setCategories(categories);
        List<Category> result = searchService.getSearchModel().getCategories();

        // then
        assertThat(result.size()).isEqualTo(categories.size());
        assertThat(result.get(0)).isEqualTo(categories.get(0));
    }

    @Test
    void canSetStarRatingTest() {
        // given
        SearchModel searchmodelDummy = searchService.getSearchModel();
        StarRatingDto starRating = createStarRatingDto();

        // when
        searchmodelDummy.setStarRating(starRating.getRating());
        Double result = searchService.getSearchModel().getStarRating();

        // then
        assertThat(result).isEqualTo(starRating.getRating());
    }

    @Test
    void canSetArchivedSearchSwitchTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        boolean searchSwitch = true;

        // when
        searchModelDummy.setArchivedSearchSwitch(searchSwitch);
        boolean result = searchService.getSearchModel().isArchivedSearchSwitch();

        // then
        assertTrue(result);
    }
}
