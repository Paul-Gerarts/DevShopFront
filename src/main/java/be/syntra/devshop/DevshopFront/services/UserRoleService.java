package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.UserRoleNotFoundException;
import be.syntra.devshop.DevshopFront.models.UserRole;
import be.syntra.devshop.DevshopFront.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    private UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole findByRoleName(String name) {
        return userRoleRepository.findUserRoleByName(name).orElseThrow(
                () -> new UserRoleNotFoundException("This userRole: " + name + " cannot be found ")
        );
    }
}
