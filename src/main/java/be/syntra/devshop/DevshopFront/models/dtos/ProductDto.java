package be.syntra.devshop.DevshopFront.models.dtos;

import be.syntra.devshop.DevshopFront.models.StarRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;

    @NotBlank(message = "This is a required field")
    private String name;

    @NotNull(message = "This is a required field")
    @PositiveOrZero(message = "Positive value required")
    @Digits(integer = 5, fraction = 2, message = "Range between 0 and 99999,99")
    private BigDecimal price;

    @NotBlank(message = "This is a required field")
    private String description;

    private boolean archived;

    @NotEmpty(message = "You must select at least one")
    private List<String> categoryNames;

    private Set<StarRating> ratings;

    @PositiveOrZero
    private double averageRating;

    @Builder.Default
    private int totalInCart = 0;
}
