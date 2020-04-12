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

    @NotBlank(message = "This is a required field")
    private String name;

    @NotNull(message = "This is a required field")
    @PositiveOrZero(message = "Positive value required")
    @Digits(integer = 5, fraction = 2, message = "Range between 0 and 99999,99")
    private BigDecimal price;

    @NotBlank(message = "This is a required field")
    private String description;
    private boolean archived;
}
