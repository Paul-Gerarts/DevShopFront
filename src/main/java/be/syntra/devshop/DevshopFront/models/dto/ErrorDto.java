package be.syntra.devshop.DevshopFront.models.dto;

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
