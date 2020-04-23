package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
    public void setSearchFailure(boolean searchFailure) {
        searchModel.setSearchFailure(searchFailure);
    }

    @Override
    public void setPriceLow(BigDecimal priceLow) {
        searchModel.setPriceLow(priceLow);
    }

    @Override
    public void setPriceHigh(BigDecimal priceHigh) {
        searchModel.setPriceHigh(priceHigh);
    }

    @Override
    public void setDescription(String description) {
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
}
