package it.danilo.blog.presentationlayer.api.exceptions.duplicated;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class DuplicateKeyException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public final HttpStatus status;
    public String key;

    public DuplicateKeyException(String key) {
        this(key, HttpStatus.CONFLICT, "Duplicate Key");
    }

    public DuplicateKeyException(String key, HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.key = key;
    }
}
