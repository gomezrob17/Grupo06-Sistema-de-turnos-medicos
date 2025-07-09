package com.Gropo06.turnos_medicos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import com.Gropo06.turnos_medicos.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;

	public SecurityConfig(CustomUserDetailsService uds) {
		this.userDetailsService = uds;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.userDetailsService(userDetailsService).authorizeHttpRequests(auth -> auth
				// Recursos estaticos, login/registro/error
				.requestMatchers("/css/**", "/images/**", "/login", "/register", "/error").permitAll()
				// Endpoint abiertos de Swagger y login REST
				.requestMatchers("/api/auth/login", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
				.requestMatchers("/api/**").permitAll()
				// Solo ROL EMPLEADO podrá acceder a /empleado/
				.requestMatchers("/empleado/**").hasRole("EMPLEADO")
				// Solo ROL PACIENTE podrá acceder a las rutas de Turnos
				.requestMatchers("/turnos/**", "/mis-turnos").hasRole("PACIENTE").requestMatchers("/paciente/**")
				.hasRole("PACIENTE")

				.anyRequest().authenticated())

				.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").usernameParameter("email")
						.successHandler(authenticationSuccessHandler()).permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout"));

		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return (request, response, authentication) -> {
			boolean isEmpleado = authentication.getAuthorities().stream()
					.anyMatch(a -> a.getAuthority().equals("ROLE_EMPLEADO"));
			if (isEmpleado) {
				response.sendRedirect(request.getContextPath() + "/empleado/dashboard");
				return;
			}

			boolean isPaciente = authentication.getAuthorities().stream()
					.anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"));
			if (isPaciente) {
				response.sendRedirect(request.getContextPath() + "/home");
				return;
			}

			response.sendRedirect(request.getContextPath() + "/home");
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
