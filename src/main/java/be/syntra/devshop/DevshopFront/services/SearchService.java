package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SearchModel;

import java.math.BigDecimal;

public interface SearchService {

    void setSearchRequest(String searchRequest);

    void setArchivedView(boolean archivedView);

    void setSearchResultView(boolean searchResultView);

    void setPriceLow(BigDecimal priceLow);

    void setPriceHigh(BigDecimal priceHigh);

    void setDescription(String description);

    void resetSearchModel();

    SearchModel getSearchModel();

    void setAppliedFiltersToSearchModel();

    void setPriceFilters(BigDecimal minPrice, BigDecimal maxPrice);

    void setSortingByName();

    void setSortingByPrice();

    void requestPreviousPage();

    void requestNextPage();

    void requestFirstPage();

    void requestLastPage(int lastPageNumber);

    void addToSelectedCategories(String category);

    void setRemainingCategories(String category);

    void removeFromSelectedCategories(String category);
}
