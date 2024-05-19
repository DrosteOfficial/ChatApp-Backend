package JSWD.Web.service;

import JSWD.Web.repositories.chatSpecific.IImageRepository;
import JSWD.Web.model.chatSpecific.Image;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;
@Service
public class ImageService {
    IImageRepository imageRepository;
    public ImageService(IImageRepository imageRepository) {
        this.imageRepository = imageRepository;

    }

    @Transactional
    public Optional<String> GetImageData(int userId){
        var imageData = imageRepository.findById((long) userId).get().getImageData();
        if (imageData.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(imageData);
        }
    }
    @Transactional
    public Optional<String> SaveImage(MultipartFile file) throws IOException {
        byte[] fileBytes = file.getBytes();

        // Encode the byte array to a Base64 string
        String base64Image = Base64.getEncoder().encodeToString(fileBytes);
        if (base64Image.isEmpty()) {
            return Optional.empty();
        }
        imageRepository.save(new Image(base64Image));

        return Optional.of(base64Image);
    }
}
