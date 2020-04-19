package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SearchModel;

public interface SearchService {

    void setSearchRequest(String searchRequest);

    SearchModel getSearchModel();
}
