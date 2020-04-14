package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dto.SearchModelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    // todo DEV-015 might use other way of creation of the SearchModelDto , for now it's by @Bean in the config

    private SearchModelDto searchModelDto;

    @Autowired
    public SearchServiceImpl(SearchModelDto searchModelDto) {
        this.searchModelDto = searchModelDto;
    }

    // todo: DEV-015 impl's from interface might replace captureSearchedTerm & getSearchModelDto

    @Override
    public void captureSearchedTerm(String searchTerm) {
        searchModelDto.setBasicSearchTerm(searchTerm);
    }

    @Override
    public SearchModelDto getSearchModelDto() {
        return searchModelDto;
    }
}