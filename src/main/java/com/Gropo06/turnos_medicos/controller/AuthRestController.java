package com.Gropo06.turnos_medicos.controller;

import com.Gropo06.turnos_medicos.dto.LoginDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Operaciones de autenticación")
public class AuthRestController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/login")
	@Operation(summary = "Login de usuario", description = "Endpoint para login. Devolvemos 200 si las credenciales son válidas, 401 si no lo son")
	public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));
			return ResponseEntity.ok("Login OK");
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(401).body("Credenciales inválidas");
		} catch (AuthenticationException e) {
			return ResponseEntity.status(401).body("Credenciales inválidas");
		}
	}
}
