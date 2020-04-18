package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SearchModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    // todo DEV-015 might use other way of creation of the SearchModelDto , for now it's by @Bean in the config

    private final SearchModel searchModel;

    @Autowired
    public SearchServiceImpl(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    // todo: DEV-015 impl's from interface might replace captureSearchRequest & getSearchModelDto

    @Override
    public void captureSearchRequest(String searchRequest) {
        searchModel.setSearchRequest(searchRequest);
    }

    @Override
    public SearchModel getSearchModel() {
        return searchModel;
    }
}
