package com.easybee.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.easybee.backend.entity.Salarie;
import com.easybee.backend.exception.IdentifiantsInvalidesException;
import com.easybee.backend.repository.SalarieRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	private static final String MOT_DE_PASSE = "secret123";
	private static final String MESSAGE_ERREUR = "Identifiant ou mot de passe incorrect !";

	@Mock
	private SalarieRepository salarieRepository;

	@InjectMocks
	private AuthService authService;

	private Salarie salarieValide() {
		Salarie salarie = new Salarie();
		salarie.setId(5L);
		salarie.setIdentifiant("jtest");
		salarie.setMotDePasse(hasher(MOT_DE_PASSE));
		return salarie;
	}

	private String hasher(String mdp) {
		String salt = BCrypt.gensalt(4);
		return BCrypt.hashpw(mdp, salt);
	}

	@Test
	void login_bonsIdentifiants_retourneLeSalarie() {
		// Arrange :
		Salarie salarie = salarieValide();
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.of(salarie));

		// Act :
		Salarie resultat = authService.login("jtest", MOT_DE_PASSE);

		// Assert :
		assertSame(salarie, resultat);
		verify(salarieRepository).findByIdentifiant("jtest");
	}

	@Test
	void login_mauvaisMotDePasse_lanceIdentifiantsInvalides() {
		// Arrange :
		Salarie salarie = salarieValide();
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.of(salarie));

		// Act :
		IdentifiantsInvalidesException e = assertThrows(IdentifiantsInvalidesException.class,
				() -> authService.login("jtest", "secret456"));

		// Assert :
		assertEquals(MESSAGE_ERREUR, e.getMessage());
	}

	@Test
	void login_identifiantInconnu_lanceIdentifiantsInvalides() {
		// Arrange :
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.empty());

		// Act :
		IdentifiantsInvalidesException e = assertThrows(IdentifiantsInvalidesException.class,
				() -> authService.login("jtest", MOT_DE_PASSE));

		// Assert :
		assertEquals(MESSAGE_ERREUR, e.getMessage());
	}

	@ParameterizedTest(name = "mot de passe [{0}]")
	@NullAndEmptySource
	@ValueSource(strings = { "null", " ", "magique", "admin" })
	void login_motDePasseMagique_lanceIdentifiantsInvalides(String mdp) {
		// Arrange :
		Salarie salarie = salarieValide();
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.of(salarie));

		// Act :
		IdentifiantsInvalidesException e = assertThrows(IdentifiantsInvalidesException.class,
				() -> authService.login("jtest", mdp));

		// Assert :
		assertEquals(MESSAGE_ERREUR, e.getMessage());
	}

	@ParameterizedTest(name = "mot de passe [{0}]")
	@NullAndEmptySource
	@ValueSource(strings = { "null", " ", "magique", "admin" })
	void login_identifiantInconnuMotDePasseMagique_lanceIdentifiantsInvalides(String mdp) {
		// Arrange :
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.empty());

		// Act :
		IdentifiantsInvalidesException e = assertThrows(IdentifiantsInvalidesException.class,
				() -> authService.login("jtest", mdp));

		// Assert :
		assertEquals(MESSAGE_ERREUR, e.getMessage());
	}

	@Test
	void login_motDePasseEgalAuHash_lanceIdentifiantsInvalides() {
		// Arrange :
		Salarie salarie = salarieValide();
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.of(salarie));

		// Act :
		IdentifiantsInvalidesException e = assertThrows(IdentifiantsInvalidesException.class,
				() -> authService.login("jtest", salarie.getMotDePasse()));

		// Assert :
		assertEquals(MESSAGE_ERREUR, e.getMessage());
	}

	@Test
	void login_motDePasseNullEtHashDeNull_lanceIdentifiantsInvalides() {
		// Arrange :
		Salarie salarie = salarieValide();
		salarie.setMotDePasse(hasher("null"));
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.of(salarie));

		// Act :
		IdentifiantsInvalidesException e = assertThrows(IdentifiantsInvalidesException.class,
				() -> authService.login("jtest", null));

		// Assert :
		assertEquals(MESSAGE_ERREUR, e.getMessage());
	}

}
