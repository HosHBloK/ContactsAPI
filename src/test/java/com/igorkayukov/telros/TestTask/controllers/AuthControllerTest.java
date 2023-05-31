package com.igorkayukov.telros.TestTask.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igorkayukov.telros.TestTask.dto.Auth.Request.LoginRequest;
import com.igorkayukov.telros.TestTask.security.JWTUtil;
import com.igorkayukov.telros.TestTask.validators.AuthRequestValidator;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

	@Mock
	private JWTUtil jwtUtil;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private AuthRequestValidator authDTOValidator;

	@InjectMocks
	private AuthController authController;

	private MockMvc mockMvc;

	@BeforeEach
	private void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	}

	@Test
	public void performLogin_returnsTokenAndStatus200_whenBindingResultHasNoErrors() throws Exception {

		LoginRequest authDTO = new LoginRequest();
		authDTO.setUsername("abc");
		authDTO.setPassword("abc");

		String json = new ObjectMapper().writeValueAsString(authDTO);
		Mockito.when(jwtUtil.generateToken(authDTO.getUsername())).thenReturn("abc");

		//@formatter:off 
		mockMvc.perform(post("/auth/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.jwt_token").exists())
		.andExpect(jsonPath("$.jwt_token").value("abc"))
		.andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void performLogin_returnsErrorResponse_whenBindingResultHasErrors() throws Exception {

		LoginRequest authDTO = new LoginRequest();

		String json = new ObjectMapper().writeValueAsString(authDTO);

		//@formatter:off 
		mockMvc.perform(post("/auth/login")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}
}