package be.syntra.devshop.DevshopFront.models.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogInDto {

    private String email;
    private String password;
}
