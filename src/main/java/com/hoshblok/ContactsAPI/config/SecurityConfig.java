package com.hoshblok.ContactsAPI.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.hoshblok.ContactsAPI.errors.ExceptionHandlerFilter;
import com.hoshblok.ContactsAPI.services.CustomUserDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomUserDetailsService userDetailsService;
	private final JWTFilter jwtFilter;
	private final ExceptionHandlerFilter exFilter;

	@Autowired
	public SecurityConfig(CustomUserDetailsService userDetailsService, JWTFilter jwtFilter,
		ExceptionHandlerFilter exFilter) {
		this.userDetailsService = userDetailsService;
		this.jwtFilter = jwtFilter;
		this.exFilter = exFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/auth/login", "/auth/refresh_tokens").permitAll()
            .anyRequest().hasRole("ADMIN")
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //@formatter:on
        
		http.addFilterBefore(exFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncoder());
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}