package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReviewDto {

    private String userName;
    private String productName;
    private String review;
}
