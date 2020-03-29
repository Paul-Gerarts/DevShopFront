package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.StatusNotification;
import be.syntra.devshop.DevshopFront.models.dto.LogInDto;

public interface AuthorisationService {

    StatusNotification login(LogInDto logInDto);
}
