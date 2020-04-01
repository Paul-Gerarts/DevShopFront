package be.syntra.devshop.DevshopFront.models;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class ApiError {

    private final String title;
    private final String message;
    private final String status;
    private final Integer code;
}
