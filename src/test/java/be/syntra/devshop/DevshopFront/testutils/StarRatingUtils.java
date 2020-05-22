package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.StarRating;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingSet;

import java.util.Set;

public class StarRatingUtils {

    public static StarRatingDto createStarRatingDto() {
        return StarRatingDto.builder()
                .rating(4D)
                .userName("paul.gerarts@juvo.be")
                .build();
    }

    public static Set<StarRating> createStarRatingSet() {
        return Set.of(StarRating.builder()
                .rating(4D)
                .userName("paul.gerarts@juvo.be")
                .build());
    }

    public static StarRatingSet createStarRatingSetDto() {
        return new StarRatingSet(createStarRatingSet());
    }
}
