package JSWD.Web.repositories.SecurityAuth;

import JSWD.Web.model.security.User;
import JSWD.Web.model.security.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String username);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String email);
    Optional<User> findByUserCredentials(UserCredentials userCredentials);

}
