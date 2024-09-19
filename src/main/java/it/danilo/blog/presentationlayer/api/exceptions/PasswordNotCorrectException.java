package it.danilo.blog.presentationlayer.api.exceptions;

import java.io.Serial;

public class PasswordNotCorrectException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public final String password;
    public final String passwordConfirmation;

    public PasswordNotCorrectException(String password, String passwordConfirmation, String message) {
        super(message);
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public PasswordNotCorrectException(String password, String passwordConfirmation) {
        this(password, passwordConfirmation, "Password are not the same" );
    }
}
