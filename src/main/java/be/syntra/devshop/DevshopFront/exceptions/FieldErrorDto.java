package be.syntra.devshop.DevshopFront.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldErrorDto {
    private String field;
    private String message;
}
