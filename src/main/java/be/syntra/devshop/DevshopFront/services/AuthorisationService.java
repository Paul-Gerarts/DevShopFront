package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.SaveStatus;
import be.syntra.devshop.DevshopFront.models.dto.LogInDto;
import org.springframework.http.ResponseEntity;

public interface AuthorisationService {

    SaveStatus login(LogInDto logInDto);
}
