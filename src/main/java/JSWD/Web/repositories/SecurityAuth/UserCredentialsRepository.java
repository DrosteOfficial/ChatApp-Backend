package JSWD.Web.repositories.SecurityAuth;

import JSWD.Web.model.security.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {
}
