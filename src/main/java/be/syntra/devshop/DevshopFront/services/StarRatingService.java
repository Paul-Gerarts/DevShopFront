package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;

public interface StarRatingService {

    StarRatingDto findBy(Long productId, String userName);
}
