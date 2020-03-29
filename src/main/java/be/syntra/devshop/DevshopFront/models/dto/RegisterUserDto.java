package be.syntra.devshop.DevshopFront.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserDto {

    @Email
    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmedPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String street;

    @NotBlank
    @Pattern(regexp = "\\d*\\w[a-zA-Z]?")
    private String number;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String city;

    @NotBlank
    private String country;
}
