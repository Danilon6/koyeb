package it.danilo.blog.config;

import io.jsonwebtoken.JwtException;
import it.danilo.blog.buisnesslayer.security.ApplicationUserDetailsService;
import it.danilo.blog.datalayer.entities.enums.JwtType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	JwtUtils jwt;

	@Autowired
	ApplicationUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {

			log.info("Processing AuthTokenFilter");

			var header = request.getHeader("Authorization");

			if (header != null && header.startsWith("Bearer ")) {

				var token = header.substring(7);
				log.info("Token: {}", token);

				if (!jwt.isTokenValid(token, JwtType.AUTHENTICATION.name()))
					throw new JwtException("token non valido");

				var email = jwt.getSubjectFromToken(token, JwtType.AUTHENTICATION.name());
				log.info("Email: {}", email);

				var details = userDetailsService.loadUserByUsername(email);
				log.info("Details: {}", details);

				var auth = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(auth);
			}

		} catch (Exception e) {
			log.error("Exception in auth filter", e);
		}

		filterChain.doFilter(request, response);
	}

}


