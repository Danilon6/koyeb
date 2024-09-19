package it.danilo.blog.buisnesslayer.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.danilo.blog.buisnesslayer.dto.ArticleRequestDTO;
import it.danilo.blog.buisnesslayer.dto.ArticleResponseDTO;
import it.danilo.blog.buisnesslayer.dto.ArticleResponsePrj;
import it.danilo.blog.buisnesslayer.services.interfaces.ArticleService;
import it.danilo.blog.buisnesslayer.services.interfaces.Mapper;
import it.danilo.blog.datalayer.entities.Article;
import it.danilo.blog.datalayer.entities.User;
import it.danilo.blog.datalayer.repositories.ArticleRepository;
import it.danilo.blog.datalayer.repositories.UserRepository;
import it.danilo.blog.presentationlayer.api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final UserRepository userRepository;

    private final Mapper<Article, ArticleResponseDTO> mapArticleEntityToArticleResponseDTO;

    private final Mapper<ArticleRequestDTO, Article> mapArticleRequestDTOToArticleEntity;

    private final Cloudinary cloudinary;

    private final CloudinaryServiceImpl cloudinaryService;

    @Override
    public List<ArticleResponsePrj> getAll() {
        return articleRepository.findAllBy();
    }

    @Override
    public ArticleResponseDTO getById(Long id) {
        Article a = articleRepository.findById(id).orElseThrow(()-> new NotFoundException(id));
        return mapArticleEntityToArticleResponseDTO.map(a);
    }

    @Override
    public ArticleResponseDTO save(ArticleRequestDTO e) throws IOException {

        Article a = mapArticleRequestDTOToArticleEntity.map(e);

       User author = userRepository.findById(e.getAuthorId()).orElseThrow(()-> new NotFoundException(e.getAuthorId()));

       a.setAuthor(author);

        if (e.getPicture() != null && !e.getPicture().isEmpty()) {
            cloudinaryService.verifyMaxSizeOfFile(e.getPicture());
            // Carica l'immagine su Cloudinary
            var url = (String) cloudinary.uploader().upload(e.getPicture().getBytes(), ObjectUtils.emptyMap()).get("url");
            a.setPicture(url);
        }

       return mapArticleEntityToArticleResponseDTO.map(articleRepository.save(a));

    }

    @Override
    public ArticleResponseDTO update(Long id, ArticleRequestDTO e) throws IOException {
        Article a = articleRepository.findById(id).orElseThrow(()-> new NotFoundException(id));

        BeanUtils.copyProperties(e, a);

        if (e.getPicture() != null && !e.getPicture().isEmpty()) {
            String url = cloudinaryService.updatePicture(id, e.getPicture());
            a.setPicture(url);
        }

        articleRepository.save(a);

        return mapArticleEntityToArticleResponseDTO.map(a);
    }

    @Override
    public ArticleResponseDTO delete(Long id) {
        Article a = articleRepository.findById(id).orElseThrow(()-> new NotFoundException(id));

        articleRepository.delete(a);

        return mapArticleEntityToArticleResponseDTO.map(a);
    }
}
