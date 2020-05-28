package be.syntra.devshop.DevshopFront.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private String userName;
    private String reviewText;
}

