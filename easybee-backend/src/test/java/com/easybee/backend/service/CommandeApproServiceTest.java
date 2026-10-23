package com.easybee.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.easybee.backend.entity.CategorieSalarie;
import com.easybee.backend.entity.CommandeAppro;
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
}
