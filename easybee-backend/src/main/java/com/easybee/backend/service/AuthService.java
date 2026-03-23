package com.easybee.backend.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.easybee.backend.entity.Salarie;
import com.easybee.backend.repository.SalarieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final SalarieRepository salarieRepository;

	public Salarie login(String identifiant, String password) {
		// 1. Chercher l'utilisateur par son identifiant
		Salarie salarie = salarieRepository.findByIdentifiant(identifiant)
				.orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

		// 2. Vérifier le mot de passe haché
		if (BCrypt.checkpw(password, salarie.getMotDePasse())) {
			return salarie; // Succès !
		} else {
			throw new RuntimeException("Mot de passe incorrect");
		}
	}
}