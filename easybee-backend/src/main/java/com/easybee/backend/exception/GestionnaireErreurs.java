package com.easybee.backend.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestionnaireErreurs {

	@ExceptionHandler(RessourceIntrouvableException.class)
	public ResponseEntity<ErreurResponse> gererRessourceIntrouvable(RessourceIntrouvableException e) {
		HttpStatus statut = HttpStatus.NOT_FOUND;
		ErreurResponse erreur = new ErreurResponse(statut.value(), e.getMessage(), LocalDateTime.now());

		return ResponseEntity.status(statut).body(erreur);
	}

	@ExceptionHandler(DonneesInvalidesException.class)
	public ResponseEntity<ErreurResponse> gererDonneesInvalides(DonneesInvalidesException e) {
		HttpStatus statut = HttpStatus.BAD_REQUEST;
		ErreurResponse erreur = new ErreurResponse(statut.value(), e.getMessage(), LocalDateTime.now());

		return ResponseEntity.status(statut).body(erreur);
	}
	
	@ExceptionHandler(ConflitException.class)
	public ResponseEntity<ErreurResponse> gererConflit(ConflitException e) {
		HttpStatus statut = HttpStatus.CONFLICT;
		ErreurResponse erreur = new ErreurResponse(statut.value(), e.getMessage(), LocalDateTime.now());

		return ResponseEntity.status(statut).body(erreur);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErreurResponse> gererIllegalArgument(IllegalArgumentException e) {
		HttpStatus statut = HttpStatus.BAD_REQUEST;
		ErreurResponse erreur = new ErreurResponse(statut.value(), "Les données entrées sont invalides ou manquantes", LocalDateTime.now());

		return ResponseEntity.status(statut).body(erreur);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErreurResponse> gererHttpMessageNotReadable(HttpMessageNotReadableException e) {
		HttpStatus statut = HttpStatus.BAD_REQUEST;
		ErreurResponse erreur = new ErreurResponse(statut.value(), "Le corps de la requête est invalide (JSON mal formé)", LocalDateTime.now());

		return ResponseEntity.status(statut).body(erreur);
	}

}
