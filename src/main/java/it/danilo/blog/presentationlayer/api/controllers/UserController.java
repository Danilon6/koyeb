package it.danilo.blog.presentationlayer.api.controllers;

import it.danilo.blog.buisnesslayer.dto.LoginResponseDTO;
import it.danilo.blog.buisnesslayer.dto.RegisterUserDTO;
import it.danilo.blog.buisnesslayer.dto.RegisteredUserDTO;
import it.danilo.blog.buisnesslayer.services.impl.UserServiceImpl;
import it.danilo.blog.datalayer.repositories.UserRepository;
import it.danilo.blog.presentationlayer.api.exceptions.ApiValidationException;
import it.danilo.blog.presentationlayer.api.exceptions.sendingEmail.EmailSendingException;
import it.danilo.blog.presentationlayer.api.models.RegisterUserModel;
import it.danilo.blog.presentationlayer.api.models.ResetPasswordModel;
import it.danilo.blog.presentationlayer.api.models.loginModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final UserRepository usersRepository;


    @PostMapping("register")
    public ResponseEntity<RegisteredUserDTO> register (@RequestBody @Validated RegisterUserModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw  new ApiValidationException(validator.getAllErrors());
        }
        RegisteredUserDTO registeredUser = userService.register(RegisterUserDTO.builder()
                .withFirstName(model.firstName())
                .withLastName(model.lastName())
                .withEmail(model.email())
                .withPassword(model.password())
                .build());

        return new ResponseEntity<>(registeredUser, HttpStatus.OK);
    }


    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login (@RequestBody @Validated loginModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw  new ApiValidationException(validator.getAllErrors());
        }
        
        return new ResponseEntity<>(userService.login(model.email(), model.password()).orElseThrow(), HttpStatus.OK);
    }


    @PostMapping("sendResetPasswordEmail")
    public ResponseEntity<String> sendResetPasswordEmail (@RequestParam String email) throws EmailSendingException {

            userService.sendResetPasswordEmail(email);
        return new ResponseEntity<>("Una nuova email per il ripristino della password è stata inviata", HttpStatus.OK);
    }

    @GetMapping("verifyResetPasswordToken")
    public ResponseEntity<Boolean> verifyResetPasswordToken (@RequestParam String token) {

        return new ResponseEntity<>(userService.verifyResetPasswordToken(token), HttpStatus.NO_CONTENT);
    }

    @PostMapping("resetPassword")
    public ResponseEntity<Boolean> resetPassword (@RequestBody @Validated ResetPasswordModel model, BindingResult validator) {
        if (validator.hasErrors()) {
            throw  new ApiValidationException(validator.getAllErrors());
        }

        return new ResponseEntity<>(userService.resetPassword(model.email(), model.password(), model.passwordConfirmation()), HttpStatus.OK);
    }

}

