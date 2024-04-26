package JSWD.Web.controler;

import JSWD.Web.model.Employee;
import JSWD.Web.model.Message;
import JSWD.Web.service.EmployeeService;
import JSWD.Web.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = Objects.requireNonNull(employeeService);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        if (employeeService.getAllEmployees().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employeeService.getAllEmployees().get());
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {

        if (employeeService.GetEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employeeService.GetEmployeeById(id).get());
    }

    @PostMapping("/employee/save")
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        if (employeeService.SaveEmployee(employee).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);

    }

    @DeleteMapping("/employee/{id}/deleteEmployee")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable int id) {
        if (employeeService.DeleteEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employeeService.DeleteEmployeeById(id).get());
    }

    @GetMapping("/employee/{id}/getImage")
    public ResponseEntity<MultipartFile> getEmployeeImage(@PathVariable int id) {
        Optional<Employee> employeeOptional = employeeService.GetEmployeeById(id);
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Employee employee = employeeOptional.get();
        String base64Image = employee.getImagedata().getImageData();

        // Decode the Base64 string to a byte array
        byte[] fileBytes = Base64.getDecoder().decode(base64Image);

        // Convert the byte array to a MultipartFile
        MultipartFile imageData = new MockMultipartFile("image.png", fileBytes);

        return ResponseEntity.ok(imageData);
    }

    @PostMapping("/employee/{id}/putImage")
    public ResponseEntity<Employee> putEmployeeImage(@PathVariable int id, @RequestBody MultipartFile imageData) throws IOException {
        if (employeeService.GetEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (imageData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        byte[] fileBytes = imageData.getBytes();

        // Encode the byte array to a Base64 string
        String base64Image = Base64.getEncoder().encodeToString(fileBytes);
        employeeService.GetEmployeeById(id).get().getImagedata().setImageData(base64Image);
        return ResponseEntity.ok(employeeService.GetEmployeeById(id).get());
    }
    @DeleteMapping("/employee/{id}/deleteImage")
    public ResponseEntity<Employee> deleteEmployeeImage(@PathVariable int id) {
        if (employeeService.GetEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeService.GetEmployeeById(id).get().getImagedata().setImageData("");
        return ResponseEntity.ok(employeeService.GetEmployeeById(id).get());
    }

    @PostMapping("/employee/{employeeId}/addMessage")
    public ResponseEntity<Employee> saveMessage( @RequestBody Message message, @PathVariable int employeeId) {
        if (employeeService.GetEmployeeById(employeeId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        employeeService.GetEmployeeById(employeeId).get().getMessages().add(message);
        return ResponseEntity.ok(employeeService.GetEmployeeById(employeeId).get());
    }
}