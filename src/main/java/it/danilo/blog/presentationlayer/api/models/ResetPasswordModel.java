package it.danilo.blog.presentationlayer.api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordModel (

        @NotBlank(message = "L'email non può contenere solo spazi vuoti")
        String email,

        @NotBlank(message = "La password non può contenere solo spazi vuoti")
        @Size(min = 8, message ="La password è troppo breve min 8 caratteri")
        String password,

        @NotBlank(message = "La conferma della password non può contenere solo spazi vuoti")
        @Size(min = 8, message ="La password è troppo breve min 8 caratteri")
        String passwordConfirmation
) {
}
