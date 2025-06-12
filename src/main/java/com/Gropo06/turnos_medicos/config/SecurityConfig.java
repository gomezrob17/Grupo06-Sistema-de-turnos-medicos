package com.Gropo06.turnos_medicos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
        http.userDetailsService(userDetailsService)
        	// Desactivamos la verificación CSRF para las rutas Rest que empiecen con /api/ permita POST sin token CSRF
        	.csrf(csrf -> csrf
        	    .ignoringRequestMatchers("/api/**")
        	  )
            .authorizeHttpRequests(auth -> auth
                // Rutas estáticas y de login/registro
            .requestMatchers("/css/**", "/images/**", "/login", "/register", "/error")
                  .permitAll()
                  
                  .requestMatchers("/api/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                   
                // Solo ROLE_EMPLEADO podrá acceder a /empleado/**
            .requestMatchers("/empleado/**")
                  .hasRole("EMPLEADO")
                // Solo ROLE_PACIENTE podrá acceder a las rutas de Turnos 
            .requestMatchers("/turnos/**", "/mis-turnos")
                  .hasRole("PACIENTE")
            .requestMatchers("/paciente/**")
                  .hasRole("PACIENTE")
                // Requerimos autenticacion
            .anyRequest()
            .authenticated()
            )
            // Configuración de formulario de login
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .successHandler(authenticationSuccessHandler())
                .permitAll()
            )
            // Configuración de logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            );

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
                // La “home” de paciente es /home.
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }

            // Por defecto, redirige a /home si el usuario no es ni EMPLEADO ni PACIENTE
            response.sendRedirect(request.getContextPath() + "/home");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
