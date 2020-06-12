package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.SearchModel;

public class SearchModelUtils {
    public static SearchModel getDummySearchModel() {
        return SearchModel.builder()
                .searchRequest("dummystring")
                .build();
    }
}
