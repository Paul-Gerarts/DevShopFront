package be.syntra.devshop.DevshopFront.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchModel {

    private String searchRequest;
    private String description;
    private String priceLow;
    private String priceHigh;
    private boolean searchFailure;
}
