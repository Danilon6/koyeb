package it.danilo.blog.presentationlayer.api.exceptions.user;

import java.io.Serial;

public class UserException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public final String email;
    public final String password;

    public UserException(String email, String password, String message) {
        super(message);
        this.email = email;
        this.password = password;
    }
}
