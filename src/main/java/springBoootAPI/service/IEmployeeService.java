package springBoootAPI.service;

import model.Employee;

import java.util.List;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    Employee GetEmployeeById(int employeeId);
    void SaveEmployee(Employee employee);
    void DeleteEmployeeById(int id);
}

