package it.danilo.blog.presentationlayer.api.exceptions.user;

import java.io.Serial;

public class InvalidLoginException extends UserException{

    @Serial
    private static final long serialVersionUID = 1L;


    public InvalidLoginException(String email, String password, String message) {
        super(email, password, message);
    }

    public InvalidLoginException(String email, String password) {
        this(email, password, "Invalid credentials" );
    }
}
