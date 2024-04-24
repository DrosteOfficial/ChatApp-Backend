package dao;

import model.Employee;

import java.util.List;

public interface IEmployeeDAO {

    List<Employee> getAllEmployees();
    Employee GetEmployeeById(int employeeId);
     void SaveEmployee(Employee employee);
     void DeleteEmployeeById(int id);
}
