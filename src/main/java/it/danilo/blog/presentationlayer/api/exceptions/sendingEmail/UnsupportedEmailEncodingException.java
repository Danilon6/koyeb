package it.danilo.blog.presentationlayer.api.exceptions.sendingEmail;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class UnsupportedEmailEncodingException extends EmailException{

    @Serial
    private static final long serialVersionUID = 1L;


    public UnsupportedEmailEncodingException(String emailTo) {
        super(emailTo, HttpStatus.BAD_REQUEST, "Unsupported encoding used in email content");
    }
}
