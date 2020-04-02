package be.syntra.devshop.DevshopFront.factories;

import be.syntra.devshop.DevshopFront.models.SecurityUser;
import be.syntra.devshop.DevshopFront.models.UserRole;
import org.junit.jupiter.api.Test;

import java.util.List;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

public class UserFactoryTest {

    private UserFactory userFactory = new UserFactory();
    private UserRoleFactory userRoleFactory = new UserRoleFactory();

    @Test
    void canCreateUserOfSecurity() {
        // given
        String userName = "test@email.com";
        String password = "test";
        String fullName = "Testy Mctest";
        List<UserRole> userRoles = List.of(userRoleFactory.of(ROLE_USER.name()));

        // when
        SecurityUser securityUserResult = userFactory.ofSecurity(userRoles, userName, password, fullName);

        // then
        assertThat(securityUserResult.getClass()).isEqualTo(SecurityUser.class);
        assertThat(securityUserResult.getUsername()).isEqualTo(fullName);
        assertThat(securityUserResult.getPassword()).isEqualTo(password);
        // assert that there are as much granted authorities as there are userRoles
        assertThat(userRoles.size()).isEqualTo(securityUserResult.getAuthorities()
                .stream()
                // assert that the granted authorities derive from the correct userRole
                .map(authority -> userRoles.stream().map(userRole -> assertThat(authority).isEqualTo(userRole.getName())))
                .count());
    }
}
