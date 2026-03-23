package com.easybee.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybee.backend.dto.LoginRequest;
import com.easybee.backend.entity.Salarie;
import com.easybee.backend.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(@RequestBody LoginRequest request) {
		try {
			Salarie salarie = authService.login(request.getIdentifiant(), request.getMotDePasse());
			return ResponseEntity.ok(salarie); // On renvoie le salarié (sans le MDP de préférence)
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
}