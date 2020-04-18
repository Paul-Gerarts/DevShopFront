package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SearchModel;

// todo: replace or alter with DEV-015

public interface SearchService {

    void captureSearchRequest(String searchRequest);

    SearchModel getSearchModel();
}
