package be.syntra.devshop.DevshopFront.models;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StarRating {

    private Long id;

    @NotBlank
    private String userName;

    @PositiveOrZero
    private double rating;
}
