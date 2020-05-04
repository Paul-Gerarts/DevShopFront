package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReviewDto {

    @NotNull
    private String userName;

    @NotBlank(message = "This is a required field.")
    private String review;
}
