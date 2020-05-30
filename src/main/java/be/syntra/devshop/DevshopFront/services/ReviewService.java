package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.StatusNotification;

public interface ReviewService {
    StatusNotification submitReview(Long productId, Review review);

    void updateReview(Long productId, Review review);

    void removeReview(Long productId, Review review);
}
