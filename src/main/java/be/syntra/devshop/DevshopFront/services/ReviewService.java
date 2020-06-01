package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.StatusNotification;

public interface ReviewService {
    StatusNotification submitReview(Long productId, Review review);

    StatusNotification updateReview(Long productId, Review review);

    StatusNotification removeReview(Long productId, Review review);

    Review findByUserNameAndId(Long id, String userName);
}
