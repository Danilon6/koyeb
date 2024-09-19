package it.danilo.blog.buisnesslayer.services.interfaces;



import it.danilo.blog.buisnesslayer.dto.ArticleResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String updatePicture(Long id, MultipartFile file) throws IOException;

    long getMaxFileSizeInBytes();

    String extractPublicIdFromUrl(String url);

    void deleteImage(String publicId) throws IOException;

    void verifyMaxSizeOfFile(MultipartFile file);
}
