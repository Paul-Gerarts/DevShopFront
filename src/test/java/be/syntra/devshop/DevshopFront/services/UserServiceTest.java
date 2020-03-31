package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.UserNotFoundException;
import be.syntra.devshop.DevshopFront.models.SecurityUser;
import be.syntra.devshop.DevshopFront.models.UserRole;
import be.syntra.devshop.DevshopFront.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_ADMIN;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void init() {
        initMocks(this);
    }

    @Test
    void findByUserNameTest() {
        // given
        String testData = "test";
        SecurityUser dummyUser = SecurityUser.builder()
                .userName(testData)
                .password(testData)
                .userRoles(
                        List.of(UserRole.builder().name(ROLE_ADMIN.name()).build()))
                .build();
        when(userRepository.findByUserName(testData)).thenReturn(ofNullable(dummyUser));

        // when
        SecurityUser resultUser = userService.findByUserName(testData);

        // then
        assertEquals(resultUser, dummyUser);
        verify(userRepository, times(1)).findByUserName(testData);
    }

    @Test
    void assert_that_UserNotFoundException_is_thrown_when_not_found(){
        // given
        String faultyUserName = "test";
        when(userRepository.findByUserName(faultyUserName)).thenThrow(new UserNotFoundException("NOT FOUND"));

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> userRepository.findByUserName(faultyUserName));
    }
}
