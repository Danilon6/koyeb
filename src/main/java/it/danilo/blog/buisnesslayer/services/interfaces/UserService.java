package it.danilo.blog.buisnesslayer.services.interfaces;

import it.danilo.blog.buisnesslayer.dto.LoginResponseDTO;
import it.danilo.blog.buisnesslayer.dto.RegisterUserDTO;
import it.danilo.blog.buisnesslayer.dto.RegisteredUserDTO;
import it.danilo.blog.presentationlayer.api.exceptions.sendingEmail.EmailSendingException;

import java.util.Optional;

public interface UserService {

    RegisteredUserDTO register(RegisterUserDTO newUser);

    Optional<LoginResponseDTO> login (String email, String password);

    void sendResetPasswordEmail (String email) throws EmailSendingException;

    boolean verifyResetPasswordToken(String token);

    boolean resetPassword (String email, String password, String passwordConfirmation);
}
