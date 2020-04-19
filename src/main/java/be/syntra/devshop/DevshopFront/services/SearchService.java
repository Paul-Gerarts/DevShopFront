package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SearchModel;

public interface SearchService {

    void setSearchRequest(String searchRequest);

    void setArchivedView(boolean archivedView);

    void setSearchResultView(boolean searchResultView);

    void setSearchFailure(boolean searchFailure);

    void resetSearchModel();

    SearchModel getSearchModel();
}
