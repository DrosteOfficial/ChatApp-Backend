package JSWD.Web.repositories;

import JSWD.Web.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {}
