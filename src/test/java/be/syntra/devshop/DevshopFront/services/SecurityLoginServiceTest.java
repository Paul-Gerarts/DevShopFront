package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.security.services.SecurityLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SecurityLoginServiceTest {

    @Autowired
    private SecurityLoginService securityLoginService;

    @Test
    void canReturnAuthTokenTest(){
        // given

        // when
        String authTokenResult = securityLoginService.login();

        // then
        assertThat(authTokenResult).isNotBlank();
        assertThat(authTokenResult.length()).isGreaterThan(64);
    }
}
