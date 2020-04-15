package be.syntra.devshop.DevshopFront.models.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDto {

    private String searchRequest;
    private String description;
    private String priceLow;
    private String priceHigh;
    private boolean searchFailure;
}
