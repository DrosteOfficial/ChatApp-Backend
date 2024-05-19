package JSWD.Web.repositories.SecurityAuth;

import JSWD.Web.model.security.token.RefreshToken;
import JSWD.Web.model.security.token.RegularToken;
import JSWD.Web.model.security.user.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;


public interface RegularTokenRepository extends JpaRepository<RegularToken, Long> {
    Optional<RegularToken> findByToken(String token);
    Collection<RegularToken> findByUserCredentials(UserCredentials userCredentials);
    @Query(
            value = "SELECT * FROM regular_token WHERE revoked=0",
            nativeQuery = true
    )
    Collection<RegularToken> findAllNotExpired();
    @Query(
            value = "SELECT * FROM refresh_token WHERE user_id=:id",
            nativeQuery = true
    )
    Collection<RegularToken> FindAllRegularTokensByUserID(@Param("id") Long id);
}
