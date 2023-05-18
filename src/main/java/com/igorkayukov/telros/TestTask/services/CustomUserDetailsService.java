package com.igorkayukov.telros.TestTask.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.igorkayukov.telros.TestTask.models.User;
import com.igorkayukov.telros.TestTask.repositories.UserRepository;
import com.igorkayukov.telros.TestTask.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> user = userRepository.findByUsername(username);

		if (user.isEmpty()) {
			throw new UsernameNotFoundException("User not found!");
		}

		return new CustomUserDetails(user.get());
	}
}