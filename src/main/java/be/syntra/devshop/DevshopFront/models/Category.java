package be.syntra.devshop.DevshopFront.models;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {

    private Long id;

    @NotBlank
    private String name;
}
