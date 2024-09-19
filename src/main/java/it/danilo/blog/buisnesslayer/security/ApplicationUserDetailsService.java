package it.danilo.blog.buisnesslayer.security;

import it.danilo.blog.datalayer.entities.User;
import it.danilo.blog.datalayer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository user;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userEntity = user.findOneByEmail(email).orElseThrow();
        return SecurityUserDetails.build(userEntity);
    }

}
