package be.syntra.devshop.DevshopFront.factories;

import be.syntra.devshop.DevshopFront.models.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserRoleFactory {

    public UserRole of(
            String userRole
    ) {
        return UserRole.builder()
                .name(userRole)
                .build();
    }
}
