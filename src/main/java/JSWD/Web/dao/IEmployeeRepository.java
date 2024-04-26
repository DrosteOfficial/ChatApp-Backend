package JSWD.Web.dao;

import JSWD.Web.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IEmployeeRepository extends JpaRepository<Employee, Long> {}
