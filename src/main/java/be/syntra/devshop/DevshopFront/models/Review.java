package be.syntra.devshop.DevshopFront.models;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    private String userName;
    @NotBlank(message = "Please fill in the review or cancel")
    private String reviewText;
}

