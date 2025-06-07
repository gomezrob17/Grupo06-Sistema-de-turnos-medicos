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
        http
            // Indicar nuestro UserDetailsService
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(auth -> auth
                // Rutas estáticas y de login/registro
                .requestMatchers("/css/**", "/images/**", "/login", "/register", "/error")
                    .permitAll()
                    .requestMatchers("/api/**").permitAll()  
                // Solo ROLE_EMPLEADO podrá acceder a /empleado/**
                .requestMatchers("/empleado/**")
                    .hasRole("EMPLEADO")
                // Solo ROLE_PACIENTE podrá acceder a las rutas de Turnos 
                .requestMatchers("/turnos/**", "/mis-turnos")
                    .hasRole("PACIENTE")
                // Si tienes alguna otra URL bajo /paciente/**, también la limitamos a ROLE_PACIENTE
                .requestMatchers("/paciente/**")
                    .hasRole("PACIENTE")
                // El resto de las rutas requiere autenticar
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
                // He supuesto que la “home” de paciente es /home. 
                // Ajusta según tu controlador si quieres enviarlo a /paciente/turnos o a otra vista.
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
