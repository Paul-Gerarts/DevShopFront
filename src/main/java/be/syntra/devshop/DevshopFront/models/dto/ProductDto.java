package be.syntra.devshop.DevshopFront.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @PositiveOrZero
    @Digits(integer = 5, fraction = 2)
    private BigDecimal price;

    @NotBlank
    private String description;
    private boolean archived;
}
