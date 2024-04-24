package JSWD.Web.service;

import JSWD.Web.dao.EmployeeDAO;
import JSWD.Web.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    EmployeeDAO employeeDAO;

    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Transactional
    @Override
    public List<Employee> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    @Transactional
    @Override
    public Employee GetEmployeeById(int employeeId) {
        Employee employee = employeeDAO.GetEmployeeById(employeeId);
        return employee;
    }

    @Transactional
    @Override
    public void SaveEmployee(Employee employee) {
        employeeDAO.SaveEmployee(employee);
    }

    @Transactional
    @Override
    public void DeleteEmployeeById(int id) {
        employeeDAO.DeleteEmployeeById(id);
    }
}
