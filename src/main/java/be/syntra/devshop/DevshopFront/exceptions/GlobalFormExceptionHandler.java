package be.syntra.devshop.DevshopFront.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalFormExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public void validationError(MethodArgumentNotValidException ex) {
        for (String code : Objects.requireNonNull(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getCodes())) {
            log.error(code);
        }
    }
}