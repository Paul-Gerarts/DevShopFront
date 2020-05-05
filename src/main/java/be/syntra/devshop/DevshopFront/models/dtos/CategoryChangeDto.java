package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryChangeDto {

    private Long categoryToChange;
    private Long categoryToSet;
}
