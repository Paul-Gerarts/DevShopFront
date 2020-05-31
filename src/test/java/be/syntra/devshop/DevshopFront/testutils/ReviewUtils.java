package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.models.Review;
import be.syntra.devshop.DevshopFront.models.dtos.ReviewDto;

public class ReviewUtils {

    public static ReviewDto getDummyReviewDto() {
        return ReviewDto.builder()
                .productId(1L)
                .userName("dummy@user.com")
                .reviewText("a dummy review")
                .build();
    }

    public static Review getDummyReview() {
        return Review.builder()
                .userName("dummy@user.com")
                .reviewText("a dummy review")
                .build();
    }
}
