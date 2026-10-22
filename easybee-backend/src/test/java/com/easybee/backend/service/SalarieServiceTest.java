package com.easybee.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.easybee.backend.entity.CategorieSalarie;
import com.easybee.backend.entity.Salarie;
import com.easybee.backend.exception.ConflitException;
import com.easybee.backend.exception.DonneesInvalidesException;
import com.easybee.backend.repository.SalarieRepository;

@ExtendWith(MockitoExtension.class)
class SalarieServiceTest {

	@Mock
	private SalarieRepository salarieRepository;

	@InjectMocks
	private SalarieService salarieService;

	private Salarie salarieValide() {
		CategorieSalarie categorie = new CategorieSalarie();
		categorie.setId(1L);

		Salarie salarie = new Salarie();
		salarie.setId(5L);
		salarie.setMatriculeSalarie(100);
		salarie.setNomSalarie("Test");
		salarie.setPrenomSalarie("Jean");
		salarie.setIdentifiant("jtest");
		salarie.setMotDePasse("secret123");
		salarie.setCategorie(categorie);
		return salarie;
	}

	@Test
	void createSalarie_valide_hacheLeMotDePasse() {
		// Arrange :
		Salarie salarie = salarieValide();
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.empty());
		when(salarieRepository.save(any(Salarie.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act :
		Salarie resultat = salarieService.createSalarie(salarie);
		assertNotEquals("secret123", resultat.getMotDePasse());

		// Assert :
		assertTrue(BCrypt.checkpw("secret123", resultat.getMotDePasse()));
		verify(salarieRepository).save(salarie);
	}

	@Test
	void createSalarie_identifiantDejaPris_lanceConflit() {
		// Arrange :
		Salarie jean = salarieValide();
		Salarie paul = salarieValide();
		paul.setId(7L);
		paul.setPrenomSalarie("Paul");
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.of(paul));

		// Act :
		ConflitException e = assertThrows(ConflitException.class, () -> salarieService.createSalarie(jean));
		assertEquals("Cet identifiant est déjà utilisé", e.getMessage());

		// Assert :
		verify(salarieRepository, never()).save(any());
	}

	@Test
	void createSalarie_sansMotDePasse_lanceDonneesInvalides() {
		// Arrange :
		Salarie salarie = salarieValide();
		salarie.setMotDePasse(null);

		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.empty());

		// Act :
		DonneesInvalidesException e = assertThrows(DonneesInvalidesException.class,
				() -> salarieService.createSalarie(salarie));
		assertEquals("Le mot de passe est obligatoire", e.getMessage());

		// Assert :
		verify(salarieRepository, never()).save(any());
	}

	@Test
	void updateSalarie_identifiantPrisParUnAutre_lanceConflit() {
		// Arrange :
		Salarie jeanEnBase = salarieValide();
		Salarie modifs = salarieValide();
		modifs.setIdentifiant("ptest");
		Salarie paul = salarieValide();
		paul.setId(7L);
		paul.setIdentifiant("ptest");

		when(salarieRepository.findById(5L)).thenReturn(Optional.of(jeanEnBase));
		when(salarieRepository.findByIdentifiant("ptest")).thenReturn(Optional.of(paul));

		// Act :
		ConflitException e = assertThrows(ConflitException.class, () -> salarieService.updateSalarie(5L, modifs));
		assertEquals("Cet identifiant est déjà utilisé", e.getMessage());

		// Assert :
		verify(salarieRepository, never()).save(any());
	}

	@Test
	void updateSalarie_garderSonPropreIdentifiant_estAccepte() {
		// Arrange :
		Salarie jeanEnBase = salarieValide();
		Salarie modifs = salarieValide();

		when(salarieRepository.findById(5L)).thenReturn(Optional.of(jeanEnBase));
		when(salarieRepository.findByIdentifiant("jtest")).thenReturn(Optional.of(jeanEnBase));
		when(salarieRepository.save(any(Salarie.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act :
		salarieService.updateSalarie(5L, modifs);

		// Assert :
		verify(salarieRepository).save(jeanEnBase);
	}

}
