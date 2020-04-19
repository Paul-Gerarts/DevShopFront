package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.TestUtils.JsonUtils;
import be.syntra.devshop.DevshopFront.TestUtils.TestWebConfig;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(SearchServiceImpl.class)
@ExtendWith(MockitoExtension.class)
@Import({TestWebConfig.class, JsonUtils.class})
public class SearchServiceTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SearchServiceImpl searchService;

    MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void canSetSearchRequestTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();

        // then
        searchModelDummy.setSearchRequest("test");
        String result = searchService.getSearchModel().getSearchRequest();

        //then
        assertThat(result).isEqualTo("test");
    }

    @Test
    void canSetArchivedViewTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        boolean isArchived = true;

        // then
        searchModelDummy.setArchivedView(isArchived);
        boolean result = searchService.getSearchModel().isArchivedView();

        //then
        assertThat(result).isEqualTo(isArchived);
    }

    @Test
    void canSetSearchResultViewTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        boolean isSearchResult = true;

        // then
        searchModelDummy.setSearchResultView(isSearchResult);
        boolean result = searchService.getSearchModel().isSearchResultView();

        //then
        assertThat(result).isEqualTo(isSearchResult);
    }

    @Test
    void canSetSearchFailureTest() {
        // given
        SearchModel searchModelDummy = searchService.getSearchModel();
        boolean isSearchFailure = true;

        // then
        searchModelDummy.setSearchFailure(isSearchFailure);
        boolean result = searchService.getSearchModel().isSearchFailure();

        //then
        assertThat(result).isEqualTo(isSearchFailure);
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
        searchModelDummy.setPriceHigh("1000");
        searchModelDummy.setPriceLow("0.01");
        searchModelDummy.setSortAscendingName(true);
        searchModelDummy.setSortAscendingPrice(true);

        // when
        searchService.resetSearchModel();

        // then
        assertThat(searchService.getSearchModel().toString()).isNotEqualTo(searchModelDummy.toString());
    }
}
