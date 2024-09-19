package it.danilo.blog.presentationlayer.api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record loginModel (
        @NotBlank(message = "L'email non può contenere solo spazi vuoti")
        String email,
        @NotBlank(message = "La password non può contenere solo spazi vuoti")
        @Size(min = 8, message ="La password è troppo breve min 20 caratteri")
        String password
) {
}
