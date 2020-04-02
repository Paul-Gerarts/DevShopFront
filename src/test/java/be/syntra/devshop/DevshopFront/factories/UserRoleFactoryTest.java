package be.syntra.devshop.DevshopFront.factories;

import be.syntra.devshop.DevshopFront.models.UserRole;
import org.junit.jupiter.api.Test;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_ADMIN;
import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRoleFactoryTest {

    private UserRoleFactory userRoleFactory = new UserRoleFactory();

    @Test
    void canCreateUserRoleTest() {
        // given
        UserRole dummyUserRole = UserRole.builder().name(ROLE_USER.name()).build();
        UserRole dummyAdminRole = UserRole.builder().name(ROLE_ADMIN.name()).build();

        // when
        UserRole resultUserRole = userRoleFactory.of(ROLE_USER.name());
        UserRole resultAdminRole = userRoleFactory.of(ROLE_ADMIN.name());

        // then
        assertThat(resultUserRole.getClass()).isEqualTo(UserRole.class);
        assertThat(resultAdminRole.getClass()).isEqualTo(UserRole.class);
        assertThat(resultUserRole.getName()).isEqualTo(dummyUserRole.getName());
        assertThat(resultAdminRole.getName()).isEqualTo(dummyAdminRole.getName());
    }
}
