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
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            )
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas para recursos estáticos y login/registro
                .requestMatchers("/css/**", "/images/**", "/login", "/register", "/error").permitAll()

                // Rutas de Swagger - documentación pública
                .requestMatchers(
                    "/swagger-ui/**", 
                    "/v3/api-docs/**", 
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()

                // Login REST
                .requestMatchers("/api/login").permitAll()

                // ⬇️ Aquí si querés que toda la API sea pública, sino podés ajustar
                .requestMatchers("/api/**").permitAll()

                // Rutas protegidas por roles
                .requestMatchers("/empleado/**").hasRole("EMPLEADO")
                .requestMatchers("/turnos/**", "/mis-turnos").hasRole("PACIENTE")
                .requestMatchers("/paciente/**").hasRole("PACIENTE")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .successHandler(authenticationSuccessHandler())
                .permitAll()
            )
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
    
}