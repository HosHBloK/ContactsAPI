package com.igorkayukov.telros.TestTask.security;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;

	public String generateToken(String username) {

		Date expirationDate = Date.from(ZonedDateTime.now().plusHours(6).toInstant());
		//@formatter:off
		return JWT.create()
			.withSubject("User details")
			.withClaim("username", username)
			.withIssuedAt(new Date())
			.withIssuer("TestTask")
			.withExpiresAt(expirationDate)
			.sign(Algorithm.HMAC256(secret));
		//@formatter:on
	}

	public Map<String, String> retrieveClaim(String token) {

		DecodedJWT jwt = validateToken(token);
		Map<String, String> claims = new HashMap<>();
		claims.put("username", jwt.getClaim("username").asString());

		return claims;
	}

	private DecodedJWT validateToken(String token) throws JWTVerificationException {
		
		//@formatter:off
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
			.withSubject("User details")
			.withIssuer("TestTask")
			.build();
		//@formatter:on
		
		return verifier.verify(token);
	}
}