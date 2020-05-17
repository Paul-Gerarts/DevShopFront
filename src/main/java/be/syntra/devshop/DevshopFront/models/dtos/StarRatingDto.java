package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StarRatingDto {

    private String userName;
    private double rating;
}
