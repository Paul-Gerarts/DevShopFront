package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;

public class StarRatingUtils {

    public static StarRatingDto createStarRatingDto() {
        return StarRatingDto.builder()
                .rating(4D)
                .userName("paul.gerarts@juvo.be")
                .build();
    }
}
