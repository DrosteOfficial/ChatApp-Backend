package JSWD.Web.controler;

import JSWD.Web.model.UserDetails;
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
    public ResponseEntity<List<UserDetails>> getAllEmployees() {
        if (userDetailsService.getAllEmployees().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDetailsService.getAllEmployees().get());
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<UserDetails> getEmployeeById(@PathVariable int id) {

        if (userDetailsService.GetEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDetailsService.GetEmployeeById(id).get());
    }

    @PostMapping("/employee/save")
    public ResponseEntity<UserDetails> saveEmployee(@RequestBody UserDetails userDetails) {
        if (userDetailsService.SaveEmployee(userDetails).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDetails);

    }

    @DeleteMapping("/employee/{id}/deleteEmployee")
    public ResponseEntity<UserDetails> deleteEmployee(@PathVariable int id) {
        if (userDetailsService.DeleteEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDetailsService.DeleteEmployeeById(id).get());
    }

    @GetMapping("/employee/{id}/getImage")
    public ResponseEntity<MultipartFile> getEmployeeImage(@PathVariable int id) {
        Optional<UserDetails> employeeOptional = userDetailsService.GetEmployeeById(id);
        if (employeeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        UserDetails userDetails = employeeOptional.get();
        String base64Image = userDetails.getImagedata().getImageData();

        // Decode the Base64 string to a byte array
        byte[] fileBytes = Base64.getDecoder().decode(base64Image);

        // Convert the byte array to a MultipartFile
        MultipartFile imageData = new MockMultipartFile("image.png", fileBytes);

        return ResponseEntity.ok(imageData);
    }

    @PostMapping("/employee/{userId}/saveImage")
    public ResponseEntity<UserDetails> saveEmployeeImage(@PathVariable int userId, @RequestBody MultipartFile imageData) throws IOException {
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
    public ResponseEntity<UserDetails> deleteEmployeeImage(@PathVariable int id) {
        if (userDetailsService.GetEmployeeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userDetailsService.GetEmployeeById(id).get().getImagedata().setImageData("");
        return ResponseEntity.ok(userDetailsService.GetEmployeeById(id).get());
    }

    @PostMapping("/employee/{employeeId}/addMessage")
    public ResponseEntity<UserDetails> saveMessage(@RequestBody Message message, @PathVariable int employeeId) {
        if (userDetailsService.GetEmployeeById(employeeId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userDetailsService.saveMessage(message, employeeId);
        return ResponseEntity.ok(userDetailsService.GetEmployeeById(employeeId).get());
    }

    @PostMapping("/employee/{employeeId}/deleteMessage/{messageId}")
    public ResponseEntity<UserDetails> deleteMessage(@PathVariable int employeeId, @PathVariable int messageId) {
        var message = userDetailsService.getMessageById(messageId);
        var employee = userDetailsService.GetEmployeeById(employeeId).get();
        employee.getMessages().removeAll(message.stream().toList());
        return ResponseEntity.ok(employee);
    }


}