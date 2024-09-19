package it.danilo.blog.presentationlayer.api.controllers;

import it.danilo.blog.buisnesslayer.dto.ArticleRequestDTO;
import it.danilo.blog.buisnesslayer.dto.ArticleResponseDTO;
import it.danilo.blog.buisnesslayer.dto.ArticleResponsePrj;
import it.danilo.blog.buisnesslayer.services.impl.UserServiceImpl;
import it.danilo.blog.buisnesslayer.services.interfaces.ArticleService;
import it.danilo.blog.buisnesslayer.services.interfaces.UserService;
import it.danilo.blog.datalayer.repositories.ArticleRepository;
import it.danilo.blog.datalayer.repositories.UserRepository;
import it.danilo.blog.presentationlayer.api.exceptions.ApiValidationException;
import it.danilo.blog.presentationlayer.api.models.ArticleRequestModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;


    @GetMapping()
    public ResponseEntity<List<ArticleResponsePrj>> getAllArticles () {
        return new ResponseEntity<>(articleService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> getArticleById (@PathVariable Long id) {
        return new ResponseEntity<>(articleService.getById(id), HttpStatus.FOUND);
    }

    @PostMapping()
    public ResponseEntity<ArticleResponseDTO> publishArticle(@RequestPart("newArticle") @Validated ArticleRequestModel model,
                                                             @RequestPart(value = "picture", required = false) MultipartFile picture,
                                                             BindingResult validator) throws IOException {

        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }

        ArticleResponseDTO article = articleService.save(ArticleRequestDTO.builder()
                .withAuthorId(model.authorId())
                .withTitle(model.title())
                .withContent(model.content())
                .withPicture(picture)
                .build());

        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> updateArticle (@PathVariable Long id, @RequestBody @Validated ArticleRequestModel model, BindingResult validator) throws IOException {
        if (validator.hasErrors()) {
            throw new ApiValidationException(validator.getAllErrors());
        }
        ArticleResponseDTO article = articleService.update(id, ArticleRequestDTO.builder()
                        .withAuthorId(model.authorId())
                        .withTitle(model.title())
                        .withContent(model.content())
                .build());

        return new ResponseEntity<>(article, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> deleteArticle (@PathVariable Long id) {
        return new ResponseEntity<>(articleService.delete(id), HttpStatus.OK);
    }
}
