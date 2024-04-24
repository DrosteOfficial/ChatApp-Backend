package dao;

import jakarta.persistence.EntityManager;
import model.Employee;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDAO implements IEmployeeDAO {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Employee> getAllEmployees() {
        Session currentSession = entityManager.unwrap(Session.class);
        Query<Employee> query = currentSession.createQuery("from Employee", Employee.class);
        List<Employee>employeeList = query.getResultList();
        return employeeList;
    }

    @Override
    public Employee GetEmployeeById(int employeeId) {
        return null;
    }

    @Override
    public void SaveEmployee(Employee employee) {

    }

    @Override
    public void DeleteEmployeeById(int id) {

    }
}
