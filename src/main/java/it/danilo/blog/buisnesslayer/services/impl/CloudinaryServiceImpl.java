package it.danilo.blog.buisnesslayer.services.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import it.danilo.blog.buisnesslayer.dto.ArticleResponseDTO;
import it.danilo.blog.buisnesslayer.services.interfaces.ImageService;
import it.danilo.blog.buisnesslayer.services.interfaces.Mapper;
import it.danilo.blog.datalayer.entities.Article;
import it.danilo.blog.datalayer.repositories.ArticleRepository;
import it.danilo.blog.presentationlayer.api.exceptions.FileSizeExceededException;
import it.danilo.blog.presentationlayer.api.exceptions.ImageDeletionException;
import it.danilo.blog.presentationlayer.api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    private final ArticleRepository articleRepository;

    private final Mapper<Article, ArticleResponseDTO> mapArticleEntityToArticleResponseDTO;

    @Override
    public String updatePicture(Long id, MultipartFile file) throws IOException {
        long maxFileSize = getMaxFileSizeInBytes();
        if (file.getSize() > maxFileSize) {
            throw new FileSizeExceededException();
        }
        var article = articleRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
        var urlExistingImage = article.getPicture();

        if (urlExistingImage != null) {
            var publicId = extractPublicIdFromUrl(urlExistingImage);
            deleteImage(publicId);
        }
        return (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");
    }


    @Override
    public void deleteImage(String publicId) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            throw new ImageDeletionException("L'ID pubblico non può essere nullo o vuoto");
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new ImageDeletionException("Eliminazione dell'immagine fallita con ID pubblico: " + publicId, e);
        }
    }

    @Override
    public String extractPublicIdFromUrl(String url) {
        String[] urlParts = url.split("/");
        String fileName = urlParts[urlParts.length - 1];
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            throw new IllegalArgumentException("URL does not contain a valid file extension");
        }
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public void verifyMaxSizeOfFile(MultipartFile file){
        long maxFileSize = getMaxFileSizeInBytes();
        if (file.getSize() > maxFileSize) {
            throw new FileSizeExceededException();
        }
    }

    @Override
    public long getMaxFileSizeInBytes() {
        String[] parts = maxFileSize.split("(?i)(?<=[0-9])(?=[a-z])");
        long size = Long.parseLong(parts[0]);
        String unit = parts[1].toUpperCase();
        switch (unit) {
            case "KB":
                size *= 1024;
                break;
            case "MB":
                size *= 1024 * 1024;
                break;
            case "GB":
                size *= 1024 * 1024 * 1024;
                break;
        }
        return size;
    }
}
