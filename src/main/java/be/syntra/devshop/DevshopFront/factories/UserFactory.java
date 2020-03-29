package be.syntra.devshop.DevshopFront.factories;

import be.syntra.devshop.DevshopFront.models.SecurityUser;
import be.syntra.devshop.DevshopFront.models.UserRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFactory {

    public SecurityUser ofSecurity(List<UserRole> roles,
                                   String userName,
                                   String password,
                                   String fullName
    ) {
        final SecurityUser securityUser = new SecurityUser();
        securityUser.setAuthorities(roles);
        securityUser.setUserName(userName);
        securityUser.setPassword(password);
        securityUser.setFullName(fullName);
        return securityUser;
    }
}
