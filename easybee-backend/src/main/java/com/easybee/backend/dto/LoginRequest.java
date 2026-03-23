package com.easybee.backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
	private String identifiant;
	private String motDePasse;
}