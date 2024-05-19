package JSWD.Web.repositories.SecurityAuth;

import JSWD.Web.model.security.user.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
}
