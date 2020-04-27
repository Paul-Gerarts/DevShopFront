package be.syntra.devshop.DevshopFront.models.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {

    private String errorCode;
    private String message;
}
