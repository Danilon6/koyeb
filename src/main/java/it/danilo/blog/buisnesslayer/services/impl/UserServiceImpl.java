package it.danilo.blog.buisnesslayer.services.impl;

import com.cloudinary.utils.ObjectUtils;
import it.danilo.blog.buisnesslayer.dto.LoginResponseDTO;
import it.danilo.blog.buisnesslayer.dto.RegisterUserDTO;
import it.danilo.blog.buisnesslayer.dto.RegisteredUserDTO;
import it.danilo.blog.buisnesslayer.security.ApplicationUserDetailsService;
import it.danilo.blog.buisnesslayer.services.interfaces.Mapper;
import it.danilo.blog.buisnesslayer.services.interfaces.UserService;
import it.danilo.blog.buisnesslayer.services.interfaces.email.MailService;
import it.danilo.blog.config.JwtUtils;
import it.danilo.blog.datalayer.entities.Roles;
import it.danilo.blog.datalayer.entities.User;
import it.danilo.blog.datalayer.entities.enums.JwtType;
import it.danilo.blog.datalayer.repositories.RolesRepository;
import it.danilo.blog.datalayer.repositories.UserRepository;
import it.danilo.blog.presentationlayer.api.exceptions.PasswordNotCorrectException;
import it.danilo.blog.presentationlayer.api.exceptions.duplicated.DuplicateEmailException;
import it.danilo.blog.presentationlayer.api.exceptions.sendingEmail.EmailSendingException;
import it.danilo.blog.presentationlayer.api.exceptions.user.InvalidLoginException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RolesRepository rolesRepository;

    private final PasswordEncoder encoder;

    private final ApplicationUserDetailsService userDetailsService;

    private final AuthenticationManager auth;

    private final JwtUtils jwt;

    private final Mapper<User, LoginResponseDTO> mapUserEntityToLoginResponseDTO;

    private final Mapper<RegisterUserDTO, User> mapRegisterUserToUserEntity;

    private final Mapper<User, RegisteredUserDTO> mapUserEntityToRegisteredUserDTO;
    private final MailService mailService;

    @Override
    public RegisteredUserDTO register(RegisterUserDTO newUser) {
        var emailDuplicated = userRepository.findOneByEmail(newUser.getEmail());

        if (emailDuplicated.isPresent()) {
            throw new DuplicateEmailException(newUser.getEmail());
        } else {
            try {

                var userEntity = mapRegisterUserToUserEntity.map(newUser);

                var p = encoder.encode(newUser.getPassword());
                log.info("Password encrypted: {}", p);
                userEntity.setPassword(p);
                Long totalUsers = userRepository.countUsers();

                Roles role;
                if (totalUsers == 0) {
                    role = rolesRepository.findOneByRoleType("SUPERADMIN")
                            .orElse(rolesRepository.save(Roles.builder().withRoleType("ADMIN").build()));
                } else {
                    role = rolesRepository.findOneByRoleType("ADMIN")
                            .orElse(rolesRepository.save(Roles.builder().withRoleType("ADMIN").build()));
                }

                if (!userEntity.getRoles().contains(role)) {
                    userEntity.getRoles().add(role);
                }

                User uEntity = userRepository.save(userEntity);

                return mapUserEntityToRegisteredUserDTO.map(uEntity);

            } catch (Exception e) {
                log.error(String.format("Exception saving user %s", userRepository), e);
                throw new RuntimeException();
            }
        }
    }

    @Override
    public Optional<LoginResponseDTO> login(String email, String password) {

        try {
            User userEntity = userRepository.findOneByEmail(email).orElseThrow(() -> new EntityNotFoundException("user not found"));
            var a = auth.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(a);
            LoginResponseDTO response = mapUserEntityToLoginResponseDTO.map(userEntity);
            response.setToken(jwt.generateToken(a));
            return Optional.of(response);
        }catch (NoSuchElementException e) {
            log.error("User not found", e);
            throw new InvalidLoginException(email, password);
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            throw new InvalidLoginException(email, password);
        }

    }

    @Override
    public void sendResetPasswordEmail(String email) throws EmailSendingException {

        User userEntity = userRepository.findOneByEmail(email).orElseThrow(()-> new EntityNotFoundException("user not found"));

        String token = jwt.generateToken(email);
        log.info(token);
        mailService.sendResetPasswordMail(email, token);

    }

    @Override
    public boolean verifyResetPasswordToken(String token) {
        try {
            if (jwt.isTokenValid(token, JwtType.RESET_PASSWORD.name())) {
                var email = jwt.getSubjectFromToken(token, JwtType.RESET_PASSWORD.name());

                var user = userRepository.findOneByEmail(email).orElseThrow(()-> new EntityNotFoundException("user not found"));

                return true;
            }
            return false;
        }catch (Exception e) {
            throw new RuntimeException("Exception in verifying reset password token");
        }
    }

    @Override
    public boolean resetPassword(String email, String password, String passwordConfirmation) {
       
            User userEntity = userRepository.findOneByEmail(email).orElseThrow(()-> new EntityNotFoundException("user not found"));

            if (Objects.equals(password, passwordConfirmation)) {
                String encodedPassword = encoder.encode(password);

                userEntity.setPassword(encodedPassword);

                userRepository.save(userEntity);

                return true;
            }

            throw new PasswordNotCorrectException(password, passwordConfirmation);

    }


}
