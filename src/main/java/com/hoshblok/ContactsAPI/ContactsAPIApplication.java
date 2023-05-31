package com.hoshblok.ContactsAPI;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hoshblok.ContactsAPI.models.User;
import com.hoshblok.ContactsAPI.repositories.UserRepository;

@SpringBootApplication
public class ContactsAPIApplication implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public ContactsAPIApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(ContactsAPIApplication.class, args);
	}

	@Override
	public void run(String... args) {

		User user = new User("admin", passwordEncoder.encode("admin"), "ROLE_ADMIN");
		userRepository.save(user);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}