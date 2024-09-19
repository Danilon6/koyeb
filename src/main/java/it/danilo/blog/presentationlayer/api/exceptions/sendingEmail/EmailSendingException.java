package it.danilo.blog.presentationlayer.api.exceptions.sendingEmail;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class EmailSendingException extends EmailException {

    @Serial
    private static final long serialVersionUID = 1L;


    public EmailSendingException(String emailTo) {
        super(emailTo, HttpStatus.BAD_REQUEST, "Failed to send email due to messaging error");
    }
}
