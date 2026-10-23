package com.easybee.backend.controller;

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
	public Salarie authenticate(@RequestBody LoginRequest request) {
		return authService.login(request.getIdentifiant(), request.getMotDePasse());
	}
}