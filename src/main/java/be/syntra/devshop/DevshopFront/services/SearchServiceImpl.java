package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SearchModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    private SearchModel searchModel;

    @Autowired
    public SearchServiceImpl(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    @Override
    public void setSearchRequest(String searchRequest) {
        resetSearchModel();
        searchModel.setSearchRequest(searchRequest);
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
        resetSearchModel();
        setAppliedFiltersToSearchModel();
        searchModel.setDescription(description);
    }

    @Override
    public SearchModel getSearchModel() {
        return searchModel;
    }

    @Override
    public void resetSearchModel() {
        this.searchModel = new SearchModel();
    }

    @Override
    public void setAppliedFiltersToSearchModel() {
        searchModel.setAppliedFiltersHeader(" with the applied filters");
        String searchRequest = hasSearchRequest()
                ? getSearchModel().getSearchRequest()
                : "";
        searchModel.setSearchRequest(searchRequest);
        searchModel.setSearchFailure(false);
        searchModel.setActiveFilters(true);
    }

    private boolean hasSearchRequest() {
        return StringUtils.hasText(getSearchModel().getSearchRequest());
    }

    /*@Override
    public void setPriceFilters(List<Product> products) {
        BigDecimal priceHigh = products.stream()
                .map(Product::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
        log.info("setPriceFilters() -> priceHigh = {}", priceHigh);
        searchModel.setPriceLow(BigDecimal.ZERO);
        searchModel.setPriceHigh(priceHigh);
    }*/

    public void setPriceFilters(BigDecimal minPrice,BigDecimal maxPrice) {
        /*BigDecimal priceHigh = products.stream()
                .map(Product::getPrice)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
        log.info("setPriceFilters() -> priceHigh = {}", priceHigh);*/
        searchModel.setPriceLow(minPrice);
        searchModel.setPriceHigh(maxPrice);
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
