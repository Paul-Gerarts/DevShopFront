package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SearchModel;
import be.syntra.devshop.DevshopFront.testutils.JsonUtils;
import be.syntra.devshop.DevshopFront.testutils.TestWebConfig;
import be.syntra.devshop.DevshopFront.testutils.WebContextTestExecutionListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static junit.framework.TestCase.*;
import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(SearchServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
@TestExecutionListeners({WebContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
class SearchServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SearchServiceImpl searchService;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

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
}
