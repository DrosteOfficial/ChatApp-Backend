package JSWD.Web.controler;

import JSWD.Web.model.Employee;
import JSWD.Web.model.Image;
import JSWD.Web.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/image")
public class ImageControler {
    private final String BASE64_REGEX = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$";
    private final Pattern BASE64_PATTERN = Pattern.compile(BASE64_REGEX);

    @Autowired
    private ImageService imageService;

    public ImageControler(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/get")
    public ResponseEntity<MultipartFile> getImageData(int userId) {
        Optional<String> imageOptional = imageService.GetImageData(userId);
        if (imageOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Decode the Base64 string to a byte array
        byte[] fileBytes = Base64.getDecoder().decode(imageOptional.get());
        if (fileBytes.length == 0) {
            return ResponseEntity.notFound().build();
        }
        // Convert the byte array to a MultipartFile
        MultipartFile imageData = new MockMultipartFile("image.png", fileBytes);
        if (imageData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageData);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveImage(@RequestBody MultipartFile file) throws IOException {
        try {
            try {
                // Convert the MultipartFile to a byte array
                byte[] fileBytes = file.getBytes();

                // Encode the byte array to a Base64 string
                String base64Image = Base64.getEncoder().encodeToString(fileBytes);

                // Save the Base64 string (image) to the database
                imageService.SaveImage(new Image(){
                    {
                        setImageData(base64Image);
                    }
                });


                return ResponseEntity.ok(base64Image);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
