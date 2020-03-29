package be.syntra.devshop.DevshopFront.repositories;

import be.syntra.devshop.DevshopFront.models.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SecurityUser, Long> {

    Optional<SecurityUser> findByUserName(String userName);
}
