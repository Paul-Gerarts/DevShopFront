package be.syntra.devshop.DevshopFront.exceptions;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends CustomException {

    public ProductNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
