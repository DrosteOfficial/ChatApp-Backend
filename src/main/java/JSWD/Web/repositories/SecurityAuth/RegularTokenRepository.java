package JSWD.Web.repositories.SecurityAuth;

import JSWD.Web.model.security.RegularToken;
import JSWD.Web.model.security.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;


public interface RegularTokenRepository extends JpaRepository<RegularToken, Long> {
    Optional<RegularToken> findByToken(String token);
    Collection<RegularToken> findByUserCredentials(UserCredentials userCredentials);

}
