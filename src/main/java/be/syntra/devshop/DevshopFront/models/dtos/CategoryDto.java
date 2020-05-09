package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class CategoryDto {

    private Long id;
    private String name;
}
