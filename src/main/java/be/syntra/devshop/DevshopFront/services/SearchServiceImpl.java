package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Product;
import be.syntra.devshop.DevshopFront.models.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private SearchModel searchModel;

    @Autowired
    public SearchServiceImpl(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    @Override
    public void setSearchRequest(String searchRequest) {
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
        return null != getSearchModel().getSearchRequest();
    }

    @Override
    public void setPriceFilters(List<Product> products) {
        BigDecimal priceHigh = products.stream().map(Product::getPrice).max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
        searchModel.setPriceLow(BigDecimal.ZERO);
        searchModel.setPriceHigh(priceHigh);
    }
}
