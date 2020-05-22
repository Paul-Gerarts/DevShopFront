package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.StarRatingDto;

public interface StarRatingService {

    StarRatingDto findByUserNameAndId(Long productId, String userName);

    StatusNotification submitRating(Long productId, Double count, String userName);
}
