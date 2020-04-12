package be.syntra.devshop.DevshopFront.exceptions;


import be.syntra.devshop.DevshopFront.models.dto.ProductDto;
import be.syntra.devshop.DevshopFront.models.dto.RegisterUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Slf4j
@ControllerAdvice
public class GlobalFormExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public String validationError(MethodArgumentNotValidException ex) {
        if (ex.getBindingResult().hasErrors()) {
            for (String code : Objects.requireNonNull(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getCodes())) {
                log.error(code);
            }
            if (isProductDto(ex)) {
                return "admin/product/addProduct";
            } else if (isRegisterDto(ex)) {
                return "/user/register";
            }
        }
        return "products";
    }

    private boolean isRegisterDto(MethodArgumentNotValidException ex) {
        return ex.getParameter().getParameterType().equals(RegisterUserDto.class);
    }

    private boolean isProductDto(MethodArgumentNotValidException ex) {
        return ex.getParameter().getParameterType().equals(ProductDto.class);
    }
}