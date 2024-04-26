package JSWD.Web.service;

import JSWD.Web.dao.IImageRepository;
import JSWD.Web.model.Image;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Optional<String> SaveImage(Image imageData) {
        if (imageData.getImageData().isEmpty()) {
            return Optional.empty();
        }
        imageRepository.save(imageData);
        return Optional.of(imageData.getImageData());
    }

    // Base64.Decoder decoder = Base64.getDecoder();
}
