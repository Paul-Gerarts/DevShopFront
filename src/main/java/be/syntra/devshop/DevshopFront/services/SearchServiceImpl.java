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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
        searchModel.setPageNumber(0);
        searchModel.setSearchRequest(searchRequest);
        searchModel.setDescription("");
    }

    @Override
    public void setArchivedView(boolean archivedView) {
        searchModel.setArchivedView(archivedView);
    }

    @Override
    public void setArchivedSearchSwitch(boolean archivedSearchSwitch) {
        searchModel.setArchivedSearchSwitch(archivedSearchSwitch);
    }

    @Override
    public void setSearchResultView(boolean searchResultView) {
        searchModel.setSearchResultView(searchResultView);
    }

    @Override
    public void setPriceLow(BigDecimal priceLow) {
        searchModel.setPageNumber(0);
        setAppliedFiltersToSearchModel();
        searchModel.setPriceLow(priceLow);
    }

    @Override
    public void setPriceHigh(BigDecimal priceHigh) {
        searchModel.setPageNumber(0);
        setAppliedFiltersToSearchModel();
        searchModel.setPriceHigh(priceHigh);
    }

    @Override
    public void setDescription(String description) {
        searchModel.setPageNumber(0);
        setAppliedFiltersToSearchModel();
        searchModel.setDescription(description);
    }

    @Override
    public SearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public void resetSearchModel() {
        int pageSize = searchModel.getPageSize();
        this.searchModel = new SearchModel();
        searchModel.setPageSize(pageSize);
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
    public void setStarRating(Double rating) {
        searchModel.setStarRating(rating);
    }

    @Override
    public void addToSelectedCategories(String category) {
        Set<String> selectedCategories = new HashSet<>(getSelectedCategoriesList());
        selectedCategories.add(category);
        searchModel.setSelectedCategories(selectedCategories.parallelStream()
                .collect(Collectors.toList()));
    }

    @Override
    public void removeFromSelectedCategories(String category) {
        List<String> selectedCategories = getSelectedCategoriesList();
        selectedCategories.remove(category);
        searchModel.setSelectedCategories(selectedCategories);
        restoreCategoryDropdown(category);
    }

    private void restoreCategoryDropdown(String category) {
        Set<Category> categoriesToSelect = new HashSet<>(searchModel.getCategories());
        categoriesToSelect.add(Category.builder().name(category).build());
        searchModel.setCategories(categoriesToSelect.parallelStream()
                .collect(Collectors.toList()));
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

    @Override
    public void requestPreviousPage() {
        searchModel.setPageNumber(searchModel.getPageNumber() - 1);
    }

    @Override
    public void requestNextPage() {
        searchModel.setPageNumber(searchModel.getPageNumber() + 1);
    }

    @Override
    public void requestFirstPage() {
        searchModel.setPageNumber(0);
    }

    @Override
    public void requestLastPage(int lastPageNumber) {
        searchModel.setPageNumber(lastPageNumber);
    }
}
