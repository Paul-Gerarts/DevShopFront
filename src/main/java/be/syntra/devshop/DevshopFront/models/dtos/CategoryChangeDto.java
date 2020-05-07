package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryChangeDto {

    private Long categoryToDelete;
    private Long categoryToSet;

    @NotBlank(message = "This is a required field")
    private String newCategoryName;
}
