package com.easybee.backend.exception;

public class ProduitIntrouvableException extends RuntimeException {

	public ProduitIntrouvableException(Long id) {
		super("Produit introuvable avec l'ID " + id + "\n");
	}
	
}
