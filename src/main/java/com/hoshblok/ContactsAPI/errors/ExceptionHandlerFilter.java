package com.hoshblok.ContactsAPI.errors;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hoshblok.ContactsAPI.exceptions.NotValidAuthException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);

		} catch (NotValidAuthException | JWTVerificationException | AuthenticationException ex) {
			response.setStatus(401);
			response.getWriter().write(ex.getMessage());
		}
	}
}