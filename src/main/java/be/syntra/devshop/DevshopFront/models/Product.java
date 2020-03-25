package be.syntra.devshop.DevshopFront.models;

import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    @PositiveOrZero
    private BigDecimal price;
}