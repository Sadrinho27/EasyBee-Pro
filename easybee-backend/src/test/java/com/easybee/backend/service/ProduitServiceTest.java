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

	@Mock // un faux repository
	private ProduitRepository produitRepository;

	@InjectMocks // le VRAI service, avec le faux repository injecté
	private ProduitService produitService;

	@Test
	void createProduit_prixNegatif_lanceDonneesInvalides() {
		// Arrange : préparer les données
		Produit produit = new Produit();
		produit.setDesignation("Miel");
		produit.setPrix(-3f);

		// Act + Assert : appeler la méthode et vérifier l'exception
		DonneesInvalidesException e = assertThrows(DonneesInvalidesException.class,
				() -> produitService.createProduit(produit));
		assertEquals("Le prix doit être supérieur à 0", e.getMessage());

		// Assert : on n'a JAMAIS essayé de sauvegarder un produit invalide
		verify(produitRepository, never()).save(any());
	}

	@Test
	void createProduit_valide_sauvegardeEtMetIdANull() {
		// Arrange : un produit VALIDE (désignation + prix), avec un id "pirate"
		Produit produit = new Produit();
		produit.setId(42L);
		produit.setDesignation("Miel");
		produit.setPrix(1.4f);

		// Le faux repository renvoie simplement ce qu'on lui donne à sauvegarder
		when(produitRepository.save(any(Produit.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act : PAS d'assertThrows, on appelle simplement la méthode
		Produit resultat = produitService.createProduit(produit);

		// Assert : l'id a été remis à null avant la sauvegarde…
		assertNull(resultat.getId());
		// … et save a bien été appelé
		verify(produitRepository).save(resultat);
	}

	@Test
	void createProduit_designationVide_lanceDonneesInvalides() {
		// Arrange : préparer les données
		Produit produit = new Produit();
		produit.setDesignation("");
		produit.setPrix(12.5f);

		// Act + Assert : appeler la méthode et vérifier l'exception
		DonneesInvalidesException e = assertThrows(DonneesInvalidesException.class,
				() -> produitService.createProduit(produit));
		assertEquals("La désignation est obligatoire", e.getMessage());

		// Assert : on n'a JAMAIS essayé de sauvegarder un produit invalide
		verify(produitRepository, never()).save(any());
	}

	@Test
	void createProduit_designationEspaces_lanceDonneesInvalides() {
		// Arrange : préparer les données
		Produit produit = new Produit();
		produit.setDesignation("   ");
		produit.setPrix(12.5f);

		// Act + Assert : appeler la méthode et vérifier l'exception
		DonneesInvalidesException e = assertThrows(DonneesInvalidesException.class,
				() -> produitService.createProduit(produit));
		assertEquals("La désignation est obligatoire", e.getMessage());

		// Assert : on n'a JAMAIS essayé de sauvegarder un produit invalide
		verify(produitRepository, never()).save(any());
	}

	@Test
	void getById_inexistant_lanceRessourceIntrouvable() {
		// Arrange : préparer les données
		when(produitRepository.findById(99999L)).thenReturn(Optional.empty());

		// Act : appeler la méthode et vérifier l'exception
		RessourceIntrouvableException e = assertThrows(RessourceIntrouvableException.class,
				() -> produitService.getById(99999L));

		// Assert
		assertEquals("Produit introuvable avec l'ID 99999", e.getMessage());
	}

	@Test
	void getById_existant_renvoieLeProduit() {
		// Arrange :
		Produit produit = new Produit();
		produit.setId(24L);
		produit.setDesignation("Miel");
		produit.setPrix(12.5f);

		when(produitRepository.findById(24L)).thenReturn(Optional.of(produit));

		// Act :
		Produit resultat = produitService.getById(24L);

		// Assert :
		assertEquals(produit, resultat);
	}

	@Test
	void deleteProduit_inexistant_neSupprimeRien() {
		// Arrange :
		when(produitRepository.existsById(99999L)).thenReturn(false);

		// Act + Assert :
		RessourceIntrouvableException e = assertThrows(RessourceIntrouvableException.class,
				() -> produitService.deleteProduit(99999L));
		assertEquals("Produit introuvable avec l'ID 99999", e.getMessage());

		// Assert :
		verify(produitRepository, never()).deleteById(any());
	}

	@Test
	void updateProduit_inexistantEtInvalide_lance404() {
		// Arrange :
		Produit produit = new Produit();
		when(produitRepository.findById(99999L)).thenReturn(Optional.empty());

		// Act + Assert :
		RessourceIntrouvableException e = assertThrows(RessourceIntrouvableException.class,
				() -> produitService.updateProduit(99999L, produit));
		assertEquals("Produit introuvable avec l'ID 99999", e.getMessage());

		// Assert :
		verify(produitRepository, never()).save(any());
	}

}
