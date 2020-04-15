package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dto.SearchDto;

// todo: replace or alter with DEV-015

public interface SearchService {
    void captureSearchedTerm(String searchTerm);

    SearchDto getSearchDto();
}
