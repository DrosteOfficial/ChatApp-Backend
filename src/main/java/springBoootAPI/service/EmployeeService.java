package springBoootAPI.service;

import dao.EmployeeDAO;
import model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class EmployeeService implements IEmployeeService{
    @Autowired
    EmployeeDAO employeeDAO;
    @Transactional
    @Override
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }
    @Transactional
    @Override
    public Employee GetEmployeeById(int employeeId) {
        return null;
    }
    @Transactional
    @Override
    public void SaveEmployee(Employee employee) {

    }
    @Transactional
    @Override
    public void DeleteEmployeeById(int id) {

    }
}
