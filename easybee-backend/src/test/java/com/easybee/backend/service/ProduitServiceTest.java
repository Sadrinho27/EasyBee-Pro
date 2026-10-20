package com.easybee.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.easybee.backend.entity.Produit;
import com.easybee.backend.exception.DonneesInvalidesException;
import com.easybee.backend.exception.RessourceIntrouvableException;
import com.easybee.backend.repository.ProduitRepository;

@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {

	@Mock
	private ProduitRepository produitRepository;

	@InjectMocks
	private ProduitService produitService;

	// Un produit valide sur tous les champs : chaque test en casse UN SEUL
	private Produit produitValide() {
		Produit produit = new Produit();
		produit.setId(24L);
		produit.setCodeProduit(1001);
		produit.setDesignation("Miel expérimental");
		produit.setPrix(15f);
		produit.setStockMagasin(12);
		produit.setStockMinimum(10);
		produit.setStockEntrepot(50);
		return produit;
	}

	// Vérifie qu'un produit est refusé à la création, avec le bon message, sans
	// rien sauvegarder
	private void assertCreationRefusee(Produit produit, String messageAttendu) {
		DonneesInvalidesException e = assertThrows(DonneesInvalidesException.class,
				() -> produitService.createProduit(produit));
		assertEquals(messageAttendu, e.getMessage());
		verify(produitRepository, never()).save(any());
	}

	// ===================== createProduit : cas valides =====================

	@Test
	void createProduit_valide_sauvegardeEtMetIdANull() {
		// Arrange : un produit valide avec un id "pirate"
		Produit produit = produitValide();
		produit.setId(42L);
		when(produitRepository.save(any(Produit.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act
		Produit resultat = produitService.createProduit(produit);

		// Assert : l'id a été remis à null, et le produit a bien été sauvegardé
		assertNull(resultat.getId());
		verify(produitRepository).save(produit);
	}

	@Test
	void createProduit_stockMagasinZero_estAccepte() {
		// Arrange : un magasin en rupture, c'est un cas normal
		Produit produit = produitValide();
		produit.setStockMagasin(0);
		when(produitRepository.save(any(Produit.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act
		Produit resultat = produitService.createProduit(produit);

		// Assert
		assertEquals(0, resultat.getStockMagasin());
		verify(produitRepository).save(produit);
	}

	// ===================== createProduit : champs obligatoires =====================

	@Test
	void createProduit_sansCodeProduit_lanceDonneesInvalides() {
		Produit produit = produitValide();
		produit.setCodeProduit(null);

		assertCreationRefusee(produit, "Le code produit est obligatoire");
	}

	@Test
	void createProduit_designationVide_lanceDonneesInvalides() {
		Produit produit = produitValide();
		produit.setDesignation("");

		assertCreationRefusee(produit, "La désignation est obligatoire");
	}

	@Test
	void createProduit_designationEspaces_lanceDonneesInvalides() {
		Produit produit = produitValide();
		produit.setDesignation("   ");

		assertCreationRefusee(produit, "La désignation est obligatoire");
	}

	@Test
	void createProduit_prixNegatif_lanceDonneesInvalides() {
		Produit produit = produitValide();
		produit.setPrix(-3f);

		assertCreationRefusee(produit, "Le prix doit être supérieur à 0");
	}

	@Test
	void createProduit_sansStockMagasin_lanceDonneesInvalides() {
		Produit produit = produitValide();
		produit.setStockMagasin(null);

		assertCreationRefusee(produit, "Le stock magasin est obligatoire");
	}

	@Test
	void createProduit_sansStockMinimum_lanceDonneesInvalides() {
		Produit produit = produitValide();
		produit.setStockMinimum(null);

		assertCreationRefusee(produit, "Le stock minimum est obligatoire");
	}

	@Test
	void createProduit_sansStockEntrepot_lanceDonneesInvalides() {
		Produit produit = produitValide();
		produit.setStockEntrepot(null);

		assertCreationRefusee(produit, "Le stock entrepot est obligatoire");
	}

	@Test
	void createProduit_stockMagasinNegatif_lanceDonneesInvalides() {
		Produit produit = produitValide();
		produit.setStockMagasin(-5);

		assertCreationRefusee(produit, "Le stock magasin ne peut pas être négatif");
	}

	@Test
	void createProduit_stockEntrepotNegatif_lanceDonneesInvalides() {
		Produit produit = produitValide();
		produit.setStockEntrepot(-1);

		assertCreationRefusee(produit, "Le stock entrepot ne peut pas être négatif");
	}

	// ===================== getById =====================

	@Test
	void getById_existant_renvoieLeProduit() {
		// Arrange
		Produit produit = produitValide();
		when(produitRepository.findById(24L)).thenReturn(Optional.of(produit));

		// Act
		Produit resultat = produitService.getById(24L);

		// Assert
		assertEquals(produit, resultat);
	}

	@Test
	void getById_inexistant_lanceRessourceIntrouvable() {
		// Arrange
		when(produitRepository.findById(99999L)).thenReturn(Optional.empty());

		// Act + Assert
		RessourceIntrouvableException e = assertThrows(RessourceIntrouvableException.class,
				() -> produitService.getById(99999L));
		assertEquals("Produit introuvable avec l'ID 99999", e.getMessage());
	}

	// ===================== deleteProduit =====================

	@Test
	void deleteProduit_inexistant_neSupprimeRien() {
		// Arrange
		when(produitRepository.existsById(99999L)).thenReturn(false);

		// Act + Assert
		RessourceIntrouvableException e = assertThrows(RessourceIntrouvableException.class,
				() -> produitService.deleteProduit(99999L));
		assertEquals("Produit introuvable avec l'ID 99999", e.getMessage());
		verify(produitRepository, never()).deleteById(any());
	}

	// ===================== updateProduit =====================

	@Test
	void updateProduit_inexistantEtInvalide_lance404() {
		// Arrange : un produit totalement vide (invalide) ET un id inexistant
		Produit produit = new Produit();
		when(produitRepository.findById(99999L)).thenReturn(Optional.empty());

		// Act + Assert : le 404 doit gagner sur le 400
		RessourceIntrouvableException e = assertThrows(RessourceIntrouvableException.class,
				() -> produitService.updateProduit(99999L, produit));
		assertEquals("Produit introuvable avec l'ID 99999", e.getMessage());
		verify(produitRepository, never()).save(any());
	}
}