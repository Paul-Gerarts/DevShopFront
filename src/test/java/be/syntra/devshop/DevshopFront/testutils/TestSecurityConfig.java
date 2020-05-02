package be.syntra.devshop.DevshopFront.testutils;

import be.syntra.devshop.DevshopFront.factories.UserFactory;
import be.syntra.devshop.DevshopFront.factories.UserRoleFactory;
import be.syntra.devshop.DevshopFront.models.SecurityUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_USER;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        UserFactory userFactory = new UserFactory();
        UserRoleFactory roleFactory = new UserRoleFactory();

        SecurityUser basicUser = userFactory.ofSecurity(
                Collections.singletonList(roleFactory.of(ROLE_USER.name())),
                "user",
                new BCryptPasswordEncoder().encode("password"),
                "Test User"
        );

        return new InMemoryUserDetailsManager(Collections.singletonList(basicUser));
    }

}