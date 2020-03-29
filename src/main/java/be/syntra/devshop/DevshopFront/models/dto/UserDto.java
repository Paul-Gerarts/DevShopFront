package be.syntra.devshop.DevshopFront.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String token;
    private String userName;
    private List<String> authorities;
    private Long userId;
    private String password;
}
