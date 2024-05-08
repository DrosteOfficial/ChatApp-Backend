package JSWD.Web.repositories.SecurityAuth;

import JSWD.Web.model.security.token.RefreshToken;
import JSWD.Web.model.security.user.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String refreshToken);

    Optional<RefreshToken> findByUserCredentials(UserCredentials userCredentials);
@Query(
        value = "SELECT * FROM refresh_token WHERE revoked=0",
        nativeQuery = true
)
Collection<RefreshToken> findAllNotRevoked();
}
