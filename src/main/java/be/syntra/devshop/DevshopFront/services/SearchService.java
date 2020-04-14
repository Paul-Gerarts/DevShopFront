package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dto.SearchModelDto;

// todo: replace or alter with DEV-015

public interface SearchService {
    void captureSearchedTerm(String searchTerm);

    SearchModelDto getSearchModelDto();
}
