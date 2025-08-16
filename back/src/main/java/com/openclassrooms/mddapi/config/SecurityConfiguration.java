package com.openclassrooms.mddapi.config;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;

import com.openclassrooms.mddapi.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
			AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.antMatchers("/api/auth/login", "/api/auth/register").permitAll()
						.anyRequest().authenticated())
				.logout(logout -> {
					logout.logoutUrl("/api/auth/logout") // Custom logout URL
							.logoutSuccessHandler(logoutSuccessHandler());
				}).cors(cors -> cors.configurationSource(request -> {
					CorsConfiguration configuration = new CorsConfiguration();
					// Only allow localhost origin
					configuration.setAllowedOriginPatterns(List.of("http://localhost:4200"));
					configuration.setAllowedMethods(List.of("POST", "GET", "OPTIONS", "PUT", "DELETE"));
					configuration.setAllowedHeaders(List.of("*"));
					configuration.setAllowCredentials(true);
					return configuration;

				}));

		http.authenticationProvider(authenticationProvider);
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	private LogoutSuccessHandler logoutSuccessHandler() {
		return (request, response, authentication) -> {
			ResponseCookie cookie = ResponseCookie.from("ACCESS_TOKEN", null).httpOnly(true).secure(true).sameSite("Strict")
					.path("/").maxAge(0).build();
			response.addHeader("Set-Cookie", cookie.toString());
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("{\"message\":\"LOGGED_OUT\"}");
			response.getWriter().flush();
		};
	}

}
