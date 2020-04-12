package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.RegisterUserDto;
import org.springframework.validation.BindingResult;

public interface AuthorisationService {

    StatusNotification register(RegisterUserDto registerUserDto);
}
