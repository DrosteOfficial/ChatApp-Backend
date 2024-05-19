package JSWD.Web.controler;

import JSWD.Web.service.ImageService;
import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final String BASE64_REGEX = "^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$";
    private final Pattern BASE64_PATTERN = Pattern.compile(BASE64_REGEX);

    @Autowired
    private ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{imageId}/get")
    public ResponseEntity<Resource> getImageData(@PathVariable int imageId) {

        Optional<String> imageOptional = imageService.GetImageData(imageId);
        if (imageOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        // Decode the Base64 string to a byte array
        byte[] fileBytes = Base64.getDecoder().decode(imageOptional.get());
        if (fileBytes.length == 0) {
            return ResponseEntity.notFound().build();
        }
        // Convert the byte array to a MultipartFile
        MultipartFile imageData = new MockMultipartFile("image", fileBytes);
        if (imageData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(imageData.getResource());
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveImage(@RequestParam MultipartFile file, Model model) throws IOException {
                if (file.isEmpty()){
                    return ResponseEntity.notFound().build();
                }
                var responce = imageService.SaveImage(file);
                if (responce.isEmpty()){
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(responce.get());
    }
}
