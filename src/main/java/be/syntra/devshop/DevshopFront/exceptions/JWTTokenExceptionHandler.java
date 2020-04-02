package be.syntra.devshop.DevshopFront.exceptions;

import be.syntra.devshop.DevshopFront.security.services.SecurityLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Objects.requireNonNull;

@ControllerAdvice
public class JWTTokenExceptionHandler {

    private SecurityLoginService securityLoginService;
    private CacheManager cacheManager;

    @Autowired
    private JWTTokenExceptionHandler(
            SecurityLoginService securityLoginService,
            CacheManager cacheManager
    ){
        this.securityLoginService = securityLoginService;
        this.cacheManager = cacheManager;
    }

    @ExceptionHandler({InvalidJWTTokenException.class})
    public void renewToken() {
        requireNonNull(cacheManager.getCache("token")).clear();
        securityLoginService.login();
    }
}
