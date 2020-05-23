package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Category;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SessionScope
@Service
public class SearchServiceImpl implements SearchService {
    private SearchModel searchModel;

    @Autowired
    public SearchServiceImpl(
            SearchModel searchModel
    ) {
        this.searchModel = searchModel;
    }

    @Override
    public void setSearchRequest(String searchRequest) {
        searchModel.setSearchRequest(searchRequest);
        searchModel.setDescription("");
    }

    @Override
    public void setArchivedView(boolean archivedView) {
        searchModel.setArchivedView(archivedView);
    }

    @Override
    public void setSearchResultView(boolean searchResultView) {
        searchModel.setSearchResultView(searchResultView);
    }

    @Override
    public void setPriceLow(BigDecimal priceLow) {
        setAppliedFiltersToSearchModel();
        searchModel.setPriceLow(priceLow);
    }

    @Override
    public void setPriceHigh(BigDecimal priceHigh) {
        setAppliedFiltersToSearchModel();
        searchModel.setPriceHigh(priceHigh);
    }

    @Override
    public void setDescription(String description) {
        setAppliedFiltersToSearchModel();
        searchModel.setDescription(description);
    }

    @Override
    public SearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public void resetSearchModel() {
        log.info("resetSearchModel()");
        this.searchModel = new SearchModel();
    }

    @Override
    public void setAppliedFiltersToSearchModel() {
        searchModel.setAppliedFiltersHeader(" with the applied filters");
        String searchRequest = hasSearchRequest()
                ? searchModel.getSearchRequest()
                : "";
        searchModel.setSearchRequest(searchRequest);
        searchModel.setSearchFailure(false);
        searchModel.setActiveFilters(true);
    }

    private boolean hasSearchRequest() {
        return StringUtils.hasText(searchModel.getSearchRequest());
    }

    @Override
    public void setPriceFilters(BigDecimal minPrice, BigDecimal maxPrice) {
        searchModel.setPriceLow(minPrice);
        searchModel.setPriceHigh(maxPrice);
    }

    @Override
    public void setSelectedCategory(String category) {
        List<String> selectedCategories = getSelectedCategoriesList();
        selectedCategories.add(category);
        searchModel.setSelectedCategories(selectedCategories);
    }

    @Override
    public void setRemainingCategories(String category) {
        List<Category> categories = searchModel.getCategories();
        searchModel.setCategories(categories.parallelStream()
                .filter(originalCategory -> !originalCategory.getName().equals(category))
                .collect(Collectors.toUnmodifiableList()));
    }

    private List<String> getSelectedCategoriesList() {
        return (null == searchModel.getSelectedCategories())
                ? new ArrayList<>()
                : searchModel.getSelectedCategories();
    }

    @Override
    public void setSortingByName() {
        if (searchModel.isNameSortActive()) {
            reverseNameSorting();
        }
        searchModel.setNameSortActive(true);
        searchModel.setPriceSortActive(false);
    }

    @Override
    public void setSortingByPrice() {
        if (searchModel.isPriceSortActive()) {
            reversePriceSorting();
        }
        searchModel.setPriceSortActive(true);
        searchModel.setNameSortActive(false);
    }

    private void reverseNameSorting() {
        boolean sortAscending = searchModel.isSortAscendingName();
        searchModel.setSortAscendingName(!sortAscending);
    }

    private void reversePriceSorting() {
        boolean sortAscending = searchModel.isSortAscendingPrice();
        searchModel.setSortAscendingPrice(!sortAscending);
    }
}
