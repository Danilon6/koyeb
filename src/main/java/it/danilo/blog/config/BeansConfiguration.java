package it.danilo.blog.config;


import com.cloudinary.Cloudinary;
import it.danilo.blog.buisnesslayer.dto.*;
import it.danilo.blog.buisnesslayer.services.interfaces.Mapper;
import it.danilo.blog.datalayer.entities.Article;
import it.danilo.blog.datalayer.entities.Roles;
import it.danilo.blog.datalayer.entities.User;
import it.danilo.blog.datalayer.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import java.util.Properties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class BeansConfiguration {

	@Autowired
	UserRepository usersRepository;


	@Value("${cloudinary.url}")
	private String cloudinaryUrl;


	@Bean
	public Cloudinary cloudinary() {
        return new Cloudinary(cloudinaryUrl);
	}


	@Bean
	public JavaMailSender getJavaMailSender(@Value("${gmail.mail.transport.protocol}" ) String protocol,
											@Value("${gmail.mail.smtp.auth}" ) String auth,
											@Value("${gmail.mail.smtp.starttls.enable}" )String starttls,
											@Value("${gmail.mail.debug}" )String debug,
											@Value("${gmail.mail.from}" )String from,
											@Value("${gmail.mail.from.password}" )String password,
											@Value("${gmail.smtp.ssl.enable}" )String ssl,
											@Value("${gmail.smtp.host}" )String host,
											@Value("${gmail.smtp.port}" )String port){

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(Integer.parseInt(port));

		mailSender.setUsername(from);
		mailSender.setPassword(password);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", protocol);
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.starttls.enable", starttls);
		props.put("mail.debug", debug);
		props.put("smtp.ssl.enable",ssl);

		return mailSender;
	}


	//Configurazione dei mapper per l'utente
	@Bean
	@Scope("singleton")
	Mapper<RegisterUserDTO, User> mapRegisterUserToUserEntity() {
		return (input) -> User.builder()
				.withFirstName(input.getFirstName())
				.withLastName(input.getLastName())
				.withEmail(input.getEmail())
				.withPassword(input.getPassword())
				.build();
	}


	@Bean
	@Scope("singleton")
	Mapper<User, RegisteredUserDTO> mapUserEntityToRegisteredUser() {
		return (input) -> RegisteredUserDTO.builder()
				.withId(input.getId())
				.withCreatedAt(input.getCreatedAt())
				.withUpdatedAt(input.getUpdatedAt())
				.withFirstName(input.getFirstName())
				.withLastName(input.getLastName())
				.withEmail(input.getEmail())
				.withRoles(input.getRoles())
				.build();
	}


	@Bean
	@Scope("singleton")
	Mapper<User, LoginResponseDTO> mapUserEntityToLoginResponse() {
		return (input) -> LoginResponseDTO.builder()
				.withUser(RegisteredUserDTO.builder()
						.withId(input.getId())
						.withCreatedAt(input.getCreatedAt())
						.withUpdatedAt(input.getUpdatedAt())
						.withFirstName(input.getFirstName())
						.withLastName(input.getLastName())
						.withEmail(input.getEmail())
						.withRoles(input.getRoles())
						.build())
				.build();
	}

	@Bean
	@Scope("singleton")
	Mapper<User, UserResponsePartialDTO> mapUserEntityToUserResponsePartialDTO () {
		return (input) -> UserResponsePartialDTO.builder()
				.withId(input.getId())
				.withFirstName(input.getFirstName())
				.withLastName(input.getLastName())
				.build();
	}


	//Mapper per i roles
	@Bean
	@Scope("singleton")
	Mapper<RolesRequestDTO, Roles> mapRolesRequestDTOToRolesEntity () {
		return (input) -> Roles.builder()
				.withRoleType(input.getRoleType())
				.build();
	}
	@Bean
	@Scope("singleton")
	Mapper<Roles, RolesResponseDTO> mapRolesEntityToRolesResponseDTO () {
		return (input) ->
				RolesResponseDTO.builder()
				.withId(input.getId())
				.withCreatedAt(input.getCreatedAt())
				.withUpdatedAt(input.getUpdatedAt())
				.withRoleType(input.getRoleType())
				.build();
	}

	//Mapper per gli articles
	@Bean
	@Scope("singleton")
	Mapper<ArticleRequestDTO, Article> mapArticleRequestDTOToArticleEntity () {
		return (input) ->
			Article.builder()
					.withContent(input.getContent())
					.withTitle(input.getTitle())
					.build();

	}

	@Bean
	@Scope("singleton")
	Mapper<Article, ArticleResponseDTO> mapArticleEntityToArticleResponseDTO () {
		return (input) -> ArticleResponseDTO.builder()
				.withId(input.getId())
				.withAuthor(mapUserEntityToUserResponsePartialDTO().map(input.getAuthor()))
				.withCreatedAt(input.getCreatedAt())
				.withUpdatedAt(input.getUpdatedAt())
				.withContent(input.getContent())
				.withTitle(input.getTitle())
				.withPicture(input.getPicture())
				.build();
	}



}
