package JSWD.Web.repositories.SecurityAuth;

import JSWD.Web.model.security.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
     Roles findByName(Roles.ERole name);
     boolean existsByName(Roles.ERole name);
}
