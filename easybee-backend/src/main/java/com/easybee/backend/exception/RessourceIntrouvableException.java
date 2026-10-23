package com.easybee.backend.exception;

public class RessourceIntrouvableException extends RuntimeException {
	public RessourceIntrouvableException(String nomRessource, Long id) {
		super(nomRessource + " introuvable avec l'ID " + id);
	}

	public RessourceIntrouvableException(String message) {
		super(message);
	}
}
