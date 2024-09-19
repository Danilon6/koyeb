package it.danilo.blog.presentationlayer.api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

public record ArticleRequestModel (
        @Positive(message = "Lo userId dell'autore dell'articolo non può essere un numero negativo")
        Long authorId,
        @NotBlank(message = "il titolo non può essere vuoto")
        String title,
        @NotBlank(message = "Il tuo articolo deve avere un contenuto")
        String content
) {
}
