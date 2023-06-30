package com.hoshblok.ContactsAPI.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoshblok.ContactsAPI.dto.auth.request.LoginRequest;
import com.hoshblok.ContactsAPI.dto.auth.response.AuthResponse;
import com.hoshblok.ContactsAPI.errors.GlobalExceptionHandler;
import com.hoshblok.ContactsAPI.security.JWTUtil;
import com.hoshblok.ContactsAPI.services.AuthService;
import com.hoshblok.ContactsAPI.validators.AuthRequestValidator;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

	@Mock
	private JWTUtil jwtUtil;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private AuthRequestValidator authRequestValidator;
	@Mock
	private AuthService authService;

	@InjectMocks
	private AuthController authController;

	private MockMvc mockMvc;

	@BeforeEach
	private void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(authController).setControllerAdvice(GlobalExceptionHandler.class)
			.build();
	}

	@Test
	public void performLogin_returnsTokensAndStatus200_whenBindingResultHasNoErrors() throws Exception {

		LoginRequest request = new LoginRequest();
		request.setUsername("abc");
		request.setPassword("abc");
		String json = new ObjectMapper().writeValueAsString(request);

		Cookie cookie = new Cookie("refreshToken", "123");
		Mockito.when(authService.getRefreshTokenCookieForLogin(any())).thenReturn(cookie);

		Mockito.when(authService.getAuthResponseForLogin(any())).thenReturn(new AuthResponse("jwt"));

		//@formatter:off 
		mockMvc.perform(post("/auth/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.jwt_access_token").exists())
		.andExpect(jsonPath("$.jwt_access_token").value("jwt"))
		.andExpect(MockMvcResultMatchers.cookie().value("refreshToken", "123"))
		.andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void performLogin_returnsErrorResponse_whenBindingResultHasErrors() throws Exception {

		LoginRequest request = new LoginRequest();

		String json = new ObjectMapper().writeValueAsString(request);

		//@formatter:off 
		mockMvc.perform(post("/auth/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
		.andExpect(status().isUnauthorized())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void refreshTokens_returnsTokensAndStatus200_whenBindingResultHasNoErrors() throws Exception {

		Cookie cookie = new Cookie("refreshToken", "123");
		Mockito.when(authService.getRefreshTokenCookieForRefresh(any())).thenReturn(cookie);

		Mockito.when(jwtUtil.verifyToken(any())).thenReturn(true);

		Mockito.when(authService.getAuthResponseForRefresh(any())).thenReturn(new AuthResponse("jwt"));

		//@formatter:off 
		mockMvc.perform(post("/auth/refresh_tokens")
			.cookie(cookie))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.jwt_access_token").exists())
		.andExpect(jsonPath("$.jwt_access_token").value("jwt"))
		.andExpect(MockMvcResultMatchers.cookie().value("refreshToken", "123"))
		.andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void refreshTokens_returnsErrorResponse_whenErrors() throws Exception {

		//@formatter:off 
		mockMvc.perform(post("/auth/refresh_tokens"))
		.andExpect(status().isUnauthorized())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}
}