package be.syntra.devshop.DevshopFront.exceptions;

import org.springframework.http.HttpStatus;

public class StarRatingNotFoundException extends CustomException {

    public StarRatingNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
