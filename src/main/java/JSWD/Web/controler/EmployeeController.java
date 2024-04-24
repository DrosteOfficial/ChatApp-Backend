package JSWD.Web.controler;

import JSWD.Web.model.Employee;
import JSWD.Web.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = Objects.requireNonNull(employeeService);
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(){
        return ResponseEntity.ok( "Hello");
    }
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
    @PostMapping
    public void save(@RequestBody Employee employee){
        employeeService.SaveEmployee(employee);
    }
    @GetMapping("/employee/{id}")
    public Employee getEmployeeById(@PathVariable int id){
        return employeeService.GetEmployeeById(id);
    }

    @DeleteMapping("/employee/{id}")
    public void deleteEmployeeById(@PathVariable int id){
        employeeService.DeleteEmployeeById(id);
    }
    @PutMapping("/employee")
    public void updateEmployee(@RequestBody Employee employee){
        employeeService.SaveEmployee(employee);
    }
}
