package be.syntra.devshop.DevshopFront.models;

import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SearchModel {
    private String searchRequest = "";
    private String description = "";
    private BigDecimal priceLow;
    private BigDecimal priceHigh;
    private boolean sortAscendingName = true;
    private boolean sortAscendingPrice;
    private boolean nameSortActive = true;
    private boolean priceSortActive;
    private boolean archivedView;
    private boolean archivedSearchSwitch;
    private boolean searchResultView;
    private boolean searchFailure;
    private boolean activeFilters;
    private String appliedFiltersHeader;
    private Integer pageNumber = 0;
    private Integer pageSize = 5;
    private List<Category> categories;
    private List<String> selectedCategories = new ArrayList<>();
    private List<Double> starRatingValues = List.of(1D, 2D, 3D, 4D, 5D);
    private Double averageRating;

    @Override
    public String toString() {
        return "SearchModel{" +
                "searchRequest='" + searchRequest + '\'' + "\n" +
                "description='" + description + '\'' + "\n" +
                "priceLow=" + priceLow + "\n" +
                "priceHigh=" + priceHigh + "\n" +
                "sortAscendingName=" + sortAscendingName + "\n" +
                "sortAscendingPrice=" + sortAscendingPrice + "\n" +
                "nameSortActive=" + nameSortActive + "\n" +
                "priceSortActive=" + priceSortActive + "\n" +
                "archivedView=" + archivedView + "\n" +
                "archivedSearchSwitch=" + archivedSearchSwitch + "\n" +
                "searchResultView=" + searchResultView + "\n" +
                "searchFailure=" + searchFailure + "\n" +
                "activeFilters=" + activeFilters + "\n" +
                "appliedFiltersHeader='" + appliedFiltersHeader + '\'' + "\n" +
                "pageNumber='" + pageNumber + '\'' + "\n" +
                "pageSize='" + pageSize + '\'' + "\n" +
                '}';
    }
}
