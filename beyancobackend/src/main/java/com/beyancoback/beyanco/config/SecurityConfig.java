package com.beyancoback.beyanco.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.beyancoback.beyanco.filter.Jwtfilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userdetailservice;

	@Autowired
	private CorsConfig corsConfig; // ✅ add this

	@Autowired
	private Jwtfilter jwtfilter;

//	@Bean
//	public SecurityFilterChain securityfilterchain(HttpSecurity http) throws Exception {
//		return http.cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())) // ✅ enable CORS
//				.csrf(Customizer -> Customizer.disable())
//				.authorizeHttpRequests(request -> request
//						.requestMatchers("/api/auth/**").permitAll()
//						.requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
//						.anyRequest()
//						.authenticated())
//				.httpBasic(Customizer.withDefaults())
//				.sessionManagement(Session -> Session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//				.addFilterAt(jwtfilter, UsernamePasswordAuthenticationFilter.class).build();
//	}
	
	@Bean
	public SecurityFilterChain securityfilterchain(HttpSecurity http) throws Exception {
	    return http
	        .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(request -> request
	            .requestMatchers("/api/auth/**").permitAll()
	            .requestMatchers("/uploads/**", "/generated/**").permitAll()
	            .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
	            .anyRequest().authenticated()
	        )
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
	        .build(); // ✅ This is the missing piece
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring()
	        .requestMatchers("/generated/**", "/uploads/**");
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userdetailservice);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
