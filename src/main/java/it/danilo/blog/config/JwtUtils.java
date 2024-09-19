package it.danilo.blog.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import it.danilo.blog.buisnesslayer.security.SecurityUserDetails;
import it.danilo.blog.datalayer.entities.enums.JwtType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

	//KEY & EXPIRATION PER IL JWT DELL'AUTENTICAZIONE
	@Value("${jwt.key.authentication}")
	private String authKey;
	@Value("${jwt.expirationMs.authentication}")
	private long authExpirationMs;

	//KEY & EXPIRATION PER IL JWT DEL RESET DELLA PASSWORD
	@Value("${jwt.key.reset.password}")
	private String validationLinkKey;
	@Value("${jwt.expirationMs.reset.password}")
	private long validationLinkExpirationMs;

	private SecretKey getKey(String key) {
		byte[] keyBytes = key.getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private long getExpiration(String type) {
		return "AUTHENTICATION".equals(type) ? authExpirationMs : validationLinkExpirationMs;
	}

	private String getKeyByType(String type) {
		return "AUTHENTICATION".equals(type) ? authKey : validationLinkKey;
	}

	public String generateToken(String email) {
		String keyStr = getKeyByType(JwtType.RESET_PASSWORD.name());
		long expirationMs = getExpiration(JwtType.RESET_PASSWORD.name());
		SecretKey key = getKey(keyStr);

        return Jwts.builder()
				.subject(email)
				.issuedAt(new Date())
				.issuer("MySpringApplication")
				.expiration(new Date(new Date().getTime() + expirationMs))
				.signWith(key)
				.compact();

	}


	public String generateToken(Authentication auth) {
		String keyStr = getKeyByType(JwtType.AUTHENTICATION.name());
		long expirationMs = getExpiration(JwtType.AUTHENTICATION.name());
		SecretKey key = getKey(keyStr);

		var user = (SecurityUserDetails) auth.getPrincipal();
		return Jwts.builder()
				.subject(user.getUsername())
				.issuedAt(new Date())
				.issuer("MySpringApplication")
				.expiration(new Date(new Date().getTime() + expirationMs))
				.signWith(key)
				.compact();

	}

	public boolean isTokenValid(String token, String type) {
		try {
			String keyStr = getKeyByType(type);
			long expirationMs = getExpiration(type);
			SecretKey key = getKey(keyStr);

			//PRENDIAMO LA DATA DI SCADENZA DAL TOKEN
			Date expirationDate = Jwts.parser()
					.verifyWith(key).build()
					.parseSignedClaims(token).getPayload().getExpiration();

			//VERIFICHIAMO SE LA DATA DI SCADENZA TROVATA E PRIMA O DOPO LA DATA DI OGGI
			if (expirationDate.before(new Date()))
				throw new JwtException("Token expired");

			Jwts.parser()
					.verifyWith(key).requireIssuer("MySpringApplication");

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getSubjectFromToken(String token, String type) {
		String keyStr = getKeyByType(type);
		SecretKey key = getKey(keyStr);
		return Jwts.parser()
				.verifyWith(key).build()
				.parseSignedClaims(token).getPayload().getSubject();
	}

}
