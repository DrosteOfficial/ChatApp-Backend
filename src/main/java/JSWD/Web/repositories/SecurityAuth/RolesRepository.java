package JSWD.Web.repositories.SecurityAuth;

import JSWD.Web.model.security.Role;
import JSWD.Web.model.security.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
     Optional<Roles> findByName(Role name);

     boolean existsByName(Role name);
}
