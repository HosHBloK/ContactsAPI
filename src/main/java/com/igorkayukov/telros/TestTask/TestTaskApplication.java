package com.igorkayukov.telros.TestTask;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.igorkayukov.telros.TestTask.models.User;
import com.igorkayukov.telros.TestTask.repositories.UserRepository;

@SpringBootApplication
public class TestTaskApplication implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public TestTaskApplication(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(TestTaskApplication.class, args);
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