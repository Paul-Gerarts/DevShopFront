package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SearchModel;

import java.math.BigDecimal;
import java.util.List;

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

    void setPriceFilters(List<Product> products);

    void setSortingByName();

    void setSortingByPrice();
}
