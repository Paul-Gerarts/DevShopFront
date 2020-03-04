package be.syntra.devshop.DevshopFront.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ProductDto {
    private String name;
    private BigDecimal price;
}
