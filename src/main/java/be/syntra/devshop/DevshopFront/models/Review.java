package be.syntra.devshop.DevshopFront.models;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Review {

    private String userName;

    @NotBlank(message = "This is a required field!")
    private String reviewText;
}
