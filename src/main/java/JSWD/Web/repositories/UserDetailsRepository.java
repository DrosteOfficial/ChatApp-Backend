package JSWD.Web.repositories;

import JSWD.Web.model.security.user.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserDetailsRepository extends JpaRepository<UserInformation, Long> {}
