package it.danilo.blog.presentationlayer.api.exceptions.sendingEmail;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class EmailException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;

    public final HttpStatus status;
    public String emailTo;

    public EmailException(String emailTo) {
        this(emailTo, HttpStatus.EXPECTATION_FAILED, "Error sending email");
    }

    public EmailException(String emailTo,  HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.emailTo = emailTo;
    }
}