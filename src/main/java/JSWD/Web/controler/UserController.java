package JSWD.Web.controler;

import JSWD.Web.model.security.user.UserInformation;
import JSWD.Web.model.Message;
import JSWD.Web.service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {
    @Autowired
    private UserDetailsService userDetailsService;


    public UserController(UserDetailsService userDetailsService) {
        this.userDetailsService = Objects.requireNonNull(userDetailsService);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<UserInformation>> getAllEmployees() {
        if (userDetailsService.getAllEmployees().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDetailsService.getAllEmployees().get());
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<UserInformation> getEmployeeById(@PathVariable int id) {

        if (userDetailsService.GetEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDetailsService.GetEmployeeById(id).get());
    }

    @PostMapping("/employee/save")
    public ResponseEntity<UserInformation> saveEmployee(@RequestBody UserInformation userInformation) {
        if (userDetailsService.SaveEmployee(userInformation).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInformation);

    }

    @DeleteMapping("/employee/{id}/deleteEmployee")
    public ResponseEntity<UserInformation> deleteEmployee(@PathVariable int id) {
        if (userDetailsService.DeleteEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDetailsService.DeleteEmployeeById(id).get());
    }

    @GetMapping("/employee/{id}/getImage")
    public ResponseEntity<MultipartFile> getEmployeeImage(@PathVariable int id) {
        Optional<UserInformation> employeeOptional = userDetailsService.GetEmployeeById(id);
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserInformation userInformation = employeeOptional.get();
        String base64Image = userInformation.getImagedata().getImageData();

        // Decode the Base64 string to a byte array
        byte[] fileBytes = Base64.getDecoder().decode(base64Image);

        // Convert the byte array to a MultipartFile
        MultipartFile imageData = new MockMultipartFile("image.png", fileBytes);

        return ResponseEntity.ok(imageData);
    }

    @PostMapping("/employee/{userId}/saveImage")
    public ResponseEntity<UserInformation> saveEmployeeImage(@PathVariable int userId, @RequestBody MultipartFile imageData) throws IOException {
        if (userDetailsService.GetEmployeeById(userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (imageData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        byte[] fileBytes = imageData.getBytes();

        // Encode the byte array to a Base64 string
        String base64Image = Base64.getEncoder().encodeToString(fileBytes);
        userDetailsService.saveUsersImage(base64Image, userId);
        return ResponseEntity.ok(userDetailsService.GetEmployeeById(userId).get());
    }

    @DeleteMapping("/employee/{id}/deleteImage")
    public ResponseEntity<UserInformation> deleteEmployeeImage(@PathVariable int id) {
        if (userDetailsService.GetEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userDetailsService.GetEmployeeById(id).get().getImagedata().setImageData("");
        return ResponseEntity.ok(userDetailsService.GetEmployeeById(id).get());
    }

    @PostMapping("/employee/{employeeId}/addMessage")
    public ResponseEntity<UserInformation> saveMessage(@RequestBody Message message, @PathVariable int employeeId) {
        if (userDetailsService.GetEmployeeById(employeeId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userDetailsService.saveMessage(message, employeeId);
        return ResponseEntity.ok(userDetailsService.GetEmployeeById(employeeId).get());
    }

    @PostMapping("/employee/{employeeId}/deleteMessage/{messageId}")
    public ResponseEntity<UserInformation> deleteMessage(@PathVariable int employeeId, @PathVariable int messageId) {
        var message = userDetailsService.getMessageById(messageId);
        var employee = userDetailsService.GetEmployeeById(employeeId).get();
        employee.getMessages().removeAll(message.stream().toList());
        return ResponseEntity.ok(employee);
    }


}