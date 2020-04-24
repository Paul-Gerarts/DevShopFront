package be.syntra.devshop.DevshopFront.models;

import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    @PositiveOrZero
    private BigDecimal price;

    @NotBlank
    private String description;

    private boolean archived;

    @Builder.Default
    private int totalInCart = 0;

    @NotEmpty
    private List<Category> categories;
}
