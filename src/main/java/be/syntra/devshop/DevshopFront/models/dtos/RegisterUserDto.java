package be.syntra.devshop.DevshopFront.models.dtos;

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

    @Email(message = "This is not a valid email")
    @NotBlank(message = "Please enter an email")
    private String userName;

    @NotBlank(message = "Fill out a password, just to be safe")
    private String password;

    @NotBlank(message = "Confirm password")
    private String confirmedPassword;

    @NotBlank(message = "This is a required field")
    private String firstName;

    @NotBlank(message = "This is a required field")
    private String lastName;

    @NotBlank(message = "This is a required field")
    private String street;

    @NotBlank(message = "This is a required field")
    @Pattern(regexp = "\\d*\\w[a-zA-Z]?", message = "Has to be a number or a number with letters")
    private String number;

    private String boxNumber;

    @NotBlank(message = "This is a required field")
    private String postalCode;

    @NotBlank(message = "This is a required field")
    private String city;

    @NotBlank(message = "This is a required field")
    private String country;
}
