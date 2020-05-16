package be.syntra.devshop.DevshopFront.models;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SearchModel {
    private String searchRequest = "";
    private String description = "";
    private BigDecimal priceLow;
    private BigDecimal priceHigh;
    private boolean sortAscendingName;
    private boolean sortAscendingPrice;
    private boolean nameSortActive = true;
    private boolean priceSortActive = true;
    private boolean archivedView;
    private boolean searchResultView;
    private boolean searchFailure;
    private boolean activeFilters;
    private String appliedFiltersHeader;

    @Override
    public String toString() {
        return "SearchModel{" +
                "searchRequest='" + searchRequest + '\'' + "\n" +
                ", description='" + description + '\'' + "\n" +
                ", priceLow=" + priceLow + "\n" +
                ", priceHigh=" + priceHigh + "\n" +
                ", sortAscendingName=" + sortAscendingName + "\n" +
                ", sortAscendingPrice=" + sortAscendingPrice + "\n" +
                ", nameSortActive=" + nameSortActive + "\n" +
                ", priceSortActive=" + priceSortActive + "\n" +
                ", archivedView=" + archivedView + "\n" +
                ", searchResultView=" + searchResultView + "\n" +
                ", searchFailure=" + searchFailure + "\n" +
                ", activeFilters=" + activeFilters + "\n" +
                ", appliedFiltersHeader='" + appliedFiltersHeader + '\'' + "\n" +
                '}';
    }
}
