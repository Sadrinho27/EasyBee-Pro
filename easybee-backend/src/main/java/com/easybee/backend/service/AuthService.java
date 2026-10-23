package com.easybee.backend.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.easybee.backend.entity.Salarie;
import com.easybee.backend.exception.IdentifiantsInvalidesException;
import com.easybee.backend.repository.SalarieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private static final String MESSAGE_ERREUR = "Identifiant ou mot de passe incorrect !";
	private static final String FAUX_HASH = BCrypt.hashpw("nimportequoi", BCrypt.gensalt(12));

	private final SalarieRepository salarieRepository;

	public Salarie login(String identifiant, String password) {
		Salarie salarie = salarieRepository.findByIdentifiant(identifiant).orElse(null);
		String hash = (salarie != null) ? salarie.getMotDePasse() : FAUX_HASH;
		boolean result = BCrypt.checkpw(password, hash);

		if (salarie != null && result) {
			return salarie;
		} else {
			throw new IdentifiantsInvalidesException(MESSAGE_ERREUR);
		}
	}
}