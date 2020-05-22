package be.syntra.devshop.DevshopFront.models.dtos;

import be.syntra.devshop.DevshopFront.models.StarRating;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StarRatingSet {

    private Set<StarRating> ratings;
}
