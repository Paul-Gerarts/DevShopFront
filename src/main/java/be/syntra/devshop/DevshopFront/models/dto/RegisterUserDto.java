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

    @Email(message = "This is not a valid email")
    @NotBlank(message = "Please enter an email")
    private String userName;

    @NotBlank(message = "Fill out a password, just to be safe")
    private String password;

    @NotBlank(message = "Confirm password")
    private String confirmedPassword;

    @NotBlank(message = "You have a first name right?")
    private String firstName;

    @NotBlank(message = "You have a last name right?")
    private String lastName;

    @NotBlank(message = "Enter your street")
    private String street;

    @NotBlank(message = "Fill out your address number")
    @Pattern(regexp = "\\d*\\w[a-zA-Z]?", message = "Has to be a number or a number with letters")
    private String number;

    private String boxNumber;

    @NotBlank(message = "Fill out your postal code")
    private String postalCode;

    @NotBlank(message = "In which city do you live in")
    private String city;

    @NotBlank(message = "In which country do you live in")
    private String country;
}
