package be.syntra.devshop.DevshopFront.init;

import be.syntra.devshop.DevshopFront.security.services.SecurityLoginService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.REQUIRED)
@NoArgsConstructor
public class SecureLogin implements ApplicationRunner {

    private SecurityLoginService secureLoginService;

    @Autowired
    public SecureLogin (SecurityLoginService securityLoginService){
        this.secureLoginService = securityLoginService;
    }

    @Override
    public void run(ApplicationArguments args) {
        secureLoginService.login();
    }
}
