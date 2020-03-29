package be.syntra.devshop.DevshopFront.services;

import be.syntra.devshop.DevshopFront.exceptions.UserNotFoundException;
import be.syntra.devshop.DevshopFront.models.SecurityUser;
import be.syntra.devshop.DevshopFront.repositories.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
@NoArgsConstructor
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public SecurityUser findByUserName(String userName) {
        Optional<SecurityUser> user = userRepository.findByUserName(userName);
        return user.orElseThrow(() -> new UserNotFoundException("This user cannot be found!"));
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        return findByUserName(s);
    }
}
