package com.igorkayukov.telros.TestTask.security;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secretKey;

	public String generateAccessToken(String username) {

		Date expirationDate = Date.from(ZonedDateTime.now().plusHours(1).toInstant());
		//@formatter:off
		return JWT.create()
			.withSubject("JWT token")
			.withClaim("username", username)
			.withIssuedAt(new Date())
			.withIssuer("TestTask")
			.withExpiresAt(expirationDate)
			.sign(Algorithm.HMAC256(secretKey));
		//@formatter:on
	}

	public String generateRefreshToken(String username) {

		Date expirationDate = Date.from(ZonedDateTime.now().plusHours(2).toInstant());
		//@formatter:off
		return JWT.create()
			.withSubject("JWT token")
			.withClaim("username", username)
			.withIssuedAt(new Date())
			.withIssuer("TestTask")
			.withExpiresAt(expirationDate)
			.sign(Algorithm.HMAC256(secretKey));
		//@formatter:on
	}

	public Map<String, String> retrieveClaim(DecodedJWT jwt) {

		Map<String, String> claims = new HashMap<>();
		claims.put("username", jwt.getClaim("username").asString());

		return claims;
	}

	public boolean verifyToken(String token) {

		try {
			getDecodedToken(token);
			return true;
			
		} catch (TokenExpiredException | JWTDecodeException ex) {

			return false;
		}
	}

	public DecodedJWT getDecodedToken(String token){

		//@formatter:off
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
			.withSubject("JWT token")
			.withIssuer("TestTask")
			.build();
		//@formatter:on

		return verifier.verify(token);
	}

	public Cookie generateRefreshTokenCookie(String username) {

		Cookie refreshTokenCookie = new Cookie("refreshToken", generateRefreshToken(username));
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(true);
		refreshTokenCookie.setMaxAge(7200);

		return refreshTokenCookie;
	}
}