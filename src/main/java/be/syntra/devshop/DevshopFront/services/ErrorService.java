package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.models.dto.ErrorDto;

import javax.servlet.http.HttpServletRequest;

public interface ErrorService {

    ErrorDto determineError(HttpServletRequest request);
}
