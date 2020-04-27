package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dtos.RegisterUserDto;

public interface AuthorisationService {

    StatusNotification register(RegisterUserDto registerUserDto);
}
