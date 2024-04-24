package controler;

import model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springBoootAPI.service.EmployeeService;

import java.util.List;

@Controller
@RequestMapping("/API")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;



    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeService.getAllEmployees();
    }
}
