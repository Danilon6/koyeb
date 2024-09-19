package it.danilo.blog.presentationlayer.api.exceptions.duplicated;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class DuplicateTitleException extends DuplicateKeyException{

    @Serial
    private static final long serialVersionUID = 1L;


    public DuplicateTitleException(String key) {
        super(key, HttpStatus.BAD_REQUEST, "Titolo duplicato");
    }
}
