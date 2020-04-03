package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.factories.UserFactory;
import be.syntra.devshop.DevshopFront.factories.UserRoleFactory;
import be.syntra.devshop.DevshopFront.models.UserRole;
import be.syntra.devshop.DevshopFront.repositories.UserRepository;
import be.syntra.devshop.DevshopFront.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_ADMIN;
import static be.syntra.devshop.DevshopFront.models.UserRoles.ROLE_USER;


@Service
public class DataFillerImpl {

    private UserRoleRepository userRoleRepository;
    private UserRoleFactory userRoleFactory;
    private UserRepository userRepository;
    private UserFactory userFactory;
    private UserRoleService userRoleService;

    @Autowired
    public DataFillerImpl(
                          UserRoleRepository userRoleRepository,
                          UserRoleFactory userRoleFactory,
                          UserRepository userRepository,
                          UserFactory userFactory,
                          UserRoleService userRoleService
    ) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleFactory = userRoleFactory;
        this.userRepository = userRepository;
        this.userFactory = userFactory;
        this.userRoleService = userRoleService;
    }

    private UserRole retrieveUserRole() {
        return userRoleService.findByRoleName(ROLE_USER.name());
    }

    private UserRole retrieveAdminRole() {
        return userRoleService.findByRoleName(ROLE_ADMIN.name());
    }

    /*
     * Checks the database for values present
     * Fills the corresponding tables that are empty with values generated
     * All generated data is optional, except the one(s) marked with DO NOT DELETE
     */
    public void initialize() {

        // DO NOT DELETE
        if (userRoleRepository.count() == 0) {
            userRoleRepository.saveAll(List.of(
                    userRoleFactory.of(ROLE_USER.name()),
                    userRoleFactory.of(ROLE_ADMIN.name())
            ));
        }

        // DO NOT DELETE
        if (userRepository.count() == 0) {
            userRepository.saveAll(List.of(
                    userFactory.ofSecurity(
                            List.of(retrieveUserRole(), retrieveAdminRole()),
                            "lens.huygh@gmail.com",
                            new BCryptPasswordEncoder().encode("Lens"),
                            "Lens Huygh"),
                    userFactory.ofSecurity(
                            List.of(retrieveUserRole(), retrieveAdminRole()),
                            "thomasf0n7a1n3@gmail.com",
                            new BCryptPasswordEncoder().encode("Thomas"),
                            "Thomas Fontaine"),
                    userFactory.ofSecurity(
                            List.of(retrieveUserRole(), retrieveAdminRole()),
                            "paul.gerarts@juvo.be",
                            new BCryptPasswordEncoder().encode("Paul"),
                            "Paul Gerarts"),
                    userFactory.ofSecurity(
                            List.of(retrieveUserRole()),
                            "user@email.com",
                            new BCryptPasswordEncoder().encode("user"),
                            "User McUserson"),
                    userFactory.ofSecurity(
                            List.of(retrieveAdminRole()),
                            "admin@email.com",
                            new BCryptPasswordEncoder().encode("admin"),
                            "Admin McAdminson"
                    )
            ));
        }
    }
}
