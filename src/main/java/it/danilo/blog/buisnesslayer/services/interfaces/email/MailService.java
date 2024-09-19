package it.danilo.blog.buisnesslayer.services.interfaces.email;


import it.danilo.blog.presentationlayer.api.exceptions.sendingEmail.EmailSendingException;
import it.danilo.blog.presentationlayer.api.exceptions.sendingEmail.UnsupportedEmailEncodingException;

public interface MailService {

    void sendResetPasswordMail(String email, String token) throws EmailSendingException;

}
