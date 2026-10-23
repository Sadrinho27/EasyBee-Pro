package com.easybee.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.easybee.backend.entity.CategorieSalarie;
import com.easybee.backend.entity.CommandeAppro;
import com.easybee.backend.entity.Produit;
import com.easybee.backend.exception.ConflitException;
import com.easybee.backend.exception.DonneesInvalidesException;
import com.easybee.backend.repository.CommandeApproRepository;
import com.easybee.backend.repository.ProduitRepository;

@ExtendWith(MockitoExtension.class)
class CommandeApproServiceTest {

	@Mock
	private CommandeApproRepository commandeApproRepository;

	@Mock
	private ProduitRepository produitRepository;

	@InjectMocks
	private CommandeApproService commandeApproService;

	private CommandeAppro commandeValide() {
		CategorieSalarie categorie = new CategorieSalarie();
		categorie.setId(1L);

		CommandeAppro commande = new CommandeAppro();
		commande.setNomCommande("Pot en verre 250g");
		commande.setQuantite(10);
		commande.setCategorieSalarie(categorie);
		return commande;
	}

	private Produit produitEnStock() {
		Produit produit = new Produit();
		produit.setId(24L);
		produit.setDesignation("Pot en verre 250g");
		produit.setStockMagasin(5);
		produit.setStockEntrepot(50);
		return produit;
	}

	@Test
	void createCommande_valide_metStatutEtDate() {
		// Arrange :
		CommandeAppro commande = commandeValide();
		when(commandeApproRepository.save(any(CommandeAppro.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act :
		CommandeAppro resultat = commandeApproService.createCommande(commande);

		// Assert :
		assertEquals("en attente", resultat.getStatutCommande());
		assertNotNull(resultat.getDateCommande());

		verify(commandeApproRepository).save(resultat);
	}

	@Test
	void createCommande_sansCategorie_lanceDonneesInvalides() {
		// Arrange :
		CommandeAppro commande = commandeValide();
		commande.setCategorieSalarie(null);

		// Act :
		DonneesInvalidesException e = assertThrows(DonneesInvalidesException.class,
				() -> commandeApproService.createCommande(commande));
		assertEquals("La catégorie est obligatoire", e.getMessage());

		// Assert :
		verify(commandeApproRepository, never()).save(any());
	}

	@Test
	void createCommande_categorieSansId_lanceDonneesInvalides() {
		// Arrange : une catégorie présente, mais sans id (comme "categorie":{} en JSON)
		CommandeAppro commande = commandeValide();
		commande.setCategorieSalarie(new CategorieSalarie());

		// Act + Assert
		DonneesInvalidesException e = assertThrows(DonneesInvalidesException.class,
				() -> commandeApproService.createCommande(commande));
		assertEquals("La catégorie est obligatoire", e.getMessage());
		verify(commandeApproRepository, never()).save(any());
	}

	@Test
	void validerCommande_stockSuffisant_transfereEntrepotVersMagasin() {
		// Arrange
		CommandeAppro commande = commandeValide();
		commande.setId(30L);
		commande.setStatutCommande("en attente");
		Produit produit = produitEnStock();

		when(commandeApproRepository.findById(30L)).thenReturn(Optional.of(commande));
		when(produitRepository.findByDesignation("Pot en verre 250g")).thenReturn(Optional.of(produit));
		when(commandeApproRepository.save(any(CommandeAppro.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act
		CommandeAppro resultat = commandeApproService.validerCommande(30L);

		// Assert
		assertEquals(15, produit.getStockMagasin());
		assertEquals(40, produit.getStockEntrepot());
		assertEquals("livrée", commande.getStatutCommande());

		verify(produitRepository).save(produit);
		verify(commandeApproRepository).save(resultat);
	}

	@Test
	void validerCommande_stockEntrepotInsuffisant_lanceConflit() {
		// Arrange
		CommandeAppro commande = commandeValide();
		commande.setId(30L);
		commande.setStatutCommande("en attente");
		Produit produit = produitEnStock();
		produit.setStockEntrepot(3);

		when(commandeApproRepository.findById(30L)).thenReturn(Optional.of(commande));
		when(produitRepository.findByDesignation("Pot en verre 250g")).thenReturn(Optional.of(produit));

		// Act
		ConflitException e = assertThrows(ConflitException.class, () -> commandeApproService.validerCommande(30L));
		assertEquals("Le stock entrepot est insuffisant", e.getMessage());

		// Assert
		assertEquals(5, produit.getStockMagasin());
		assertEquals(3, produit.getStockEntrepot());
		verify(commandeApproRepository, never()).save(any());
		verify(produitRepository, never()).save(any());
	}

	@Test
	void validerCommande_stockEntrepotExact_estAccepte() {
		// Arrange
		CommandeAppro commande = commandeValide();
		commande.setId(30L);
		commande.setStatutCommande("en attente");
		Produit produit = produitEnStock();
		produit.setStockEntrepot(10);

		when(commandeApproRepository.findById(30L)).thenReturn(Optional.of(commande));
		when(produitRepository.findByDesignation("Pot en verre 250g")).thenReturn(Optional.of(produit));
		when(commandeApproRepository.save(any(CommandeAppro.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act
		CommandeAppro resultat = commandeApproService.validerCommande(30L);

		// Assert
		assertEquals(15, produit.getStockMagasin());
		assertEquals(0, produit.getStockEntrepot());
		assertEquals("livrée", commande.getStatutCommande());

		verify(produitRepository).save(produit);
		verify(commandeApproRepository).save(resultat);
	}

	@Test
	void validerCommande_dejaLivree_lanceConflit() {
		// Arrange
		CommandeAppro commande = commandeValide();
		commande.setId(30L);
		commande.setStatutCommande("livrée");
		when(commandeApproRepository.findById(30L)).thenReturn(Optional.of(commande));

		// Act
		ConflitException e = assertThrows(ConflitException.class, () -> commandeApproService.validerCommande(30L));
		assertEquals("Cette commande est déjà validée !", e.getMessage());

		// Assert
		verify(commandeApproRepository, never()).save(any());
		verify(produitRepository, never()).save(any());
	}

	@Test
	void validerCommande_sansQuantite_transfereUneUnite() {
		// Arrange
		CommandeAppro commande = commandeValide();
		commande.setId(30L);
		commande.setStatutCommande("en attente");
		commande.setQuantite(null);
		Produit produit = produitEnStock();

		when(commandeApproRepository.findById(30L)).thenReturn(Optional.of(commande));
		when(produitRepository.findByDesignation("Pot en verre 250g")).thenReturn(Optional.of(produit));
		when(commandeApproRepository.save(any(CommandeAppro.class))).thenAnswer(inv -> inv.getArgument(0));

		// Act
		CommandeAppro resultat = commandeApproService.validerCommande(30L);

		// Assert
		assertEquals(6, produit.getStockMagasin());
		assertEquals(49, produit.getStockEntrepot());
		assertEquals("livrée", commande.getStatutCommande());

		verify(produitRepository).save(produit);
		verify(commandeApproRepository).save(resultat);
	}
}
