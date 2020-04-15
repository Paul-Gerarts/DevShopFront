package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    // todo DEV-015 might use other way of creation of the SearchModelDto , for now it's by @Bean in the config

    private SearchDto searchDto;

    @Autowired
    public SearchServiceImpl(SearchDto searchDto) {
        this.searchDto = searchDto;
    }

    // todo: DEV-015 impl's from interface might replace captureSearchedTerm & getSearchModelDto

    @Override
    public void captureSearchedTerm(String searchTerm) {
        searchDto.setBasicSearchTerm(searchTerm);
    }

    @Override
    public SearchDto getSearchDto() {
        return searchDto;
    }
}
