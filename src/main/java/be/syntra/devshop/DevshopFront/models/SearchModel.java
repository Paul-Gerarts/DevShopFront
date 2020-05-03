package be.syntra.devshop.DevshopFront.models;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SearchModel {
    private String searchRequest;
    private String description;
    private BigDecimal priceLow;
    private BigDecimal priceHigh;
    private boolean sortAscendingName;
    private boolean sortAscendingPrice;
    private boolean archivedView;
    private boolean searchResultView;
    private boolean searchFailure;
    private boolean activeFilters;
    private String appliedFiltersHeader;
}
