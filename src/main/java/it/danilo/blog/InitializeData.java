package it.danilo.blog;

import it.danilo.blog.datalayer.entities.Roles;
import it.danilo.blog.datalayer.entities.User;
import it.danilo.blog.datalayer.repositories.RolesRepository;
import it.danilo.blog.datalayer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitializeData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder encoder;


    @Override
    public void run(String... args) throws Exception {
            if (userRepository.countUsers() == 0) {
                Roles role = Roles.builder()
                        .withRoleType("SUPERADMIN")
                        .build();

                User user = User.builder()
                        .withFirstName("Danilo")
                        .withLastName("Nebulosi")
                        .withEmail("danilonebulosi@gmail.com")
                        .withPassword(encoder.encode("password"))
                        .build();

                if (!user.getRoles().contains(role)) {
                    user.getRoles().add(role);
                }

                userRepository.save(user);
            }

    }
}
