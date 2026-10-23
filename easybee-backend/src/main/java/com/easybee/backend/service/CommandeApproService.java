package com.easybee.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.easybee.backend.entity.CommandeAppro;
import com.easybee.backend.entity.Produit;
import com.easybee.backend.exception.ConflitException;
import com.easybee.backend.exception.DonneesInvalidesException;
import com.easybee.backend.exception.RessourceIntrouvableException;
import com.easybee.backend.repository.CommandeApproRepository;
import com.easybee.backend.repository.ProduitRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandeApproService {

	private final CommandeApproRepository commandeApproRepository;
	private final ProduitRepository produitRepo;

	public CommandeAppro createCommande(CommandeAppro nouvelleCommande) {
		this.verifierCommande(nouvelleCommande);

		nouvelleCommande.setStatutCommande("en attente");
		nouvelleCommande.setDateCommande(LocalDateTime.now());

		return commandeApproRepository.save(nouvelleCommande);
	}

	public List<CommandeAppro> getAll() {
		return commandeApproRepository.findAll();
	}

	public CommandeAppro getById(Long id) {
		return commandeApproRepository.findById(id)
				.orElseThrow(() -> new RessourceIntrouvableException("Commande", id));
	}

	@Transactional
	public CommandeAppro validerCommande(Long id) {
		// 1. Trouver la commande
		CommandeAppro commande = commandeApproRepository.findById(id)
				.orElseThrow(() -> new RessourceIntrouvableException("Commande", id));

		if ("livrée".equals(commande.getStatutCommande())) {
			throw new ConflitException("Cette commande est déjà validée !");
		}

		// 2. Trouver le produit correspondant
		Produit produit = produitRepo.findByDesignation(commande.getNomCommande()).orElseThrow(
				() -> new ConflitException("Aucun produit ne correspond à " + "'" + commande.getNomCommande() + "'"));

		// 3. Mise à jour du stock avec la VRAIE quantité
		int quantiteAAjouter = (commande.getQuantite() != null) ? commande.getQuantite() : 1;
		if (produit.getStockEntrepot() < quantiteAAjouter) {
			throw new ConflitException("Le stock entrepot est insuffisant");
		}
		produit.setStockMagasin(produit.getStockMagasin() + quantiteAAjouter);
		produit.setStockEntrepot(produit.getStockEntrepot() - quantiteAAjouter);
		produitRepo.save(produit);

		// 4. Changer le statut de la commande
		commande.setStatutCommande("livrée");
		return commandeApproRepository.save(commande);
	}

	private void verifierCommande(CommandeAppro commande) {
		if (commande.getNomCommande() == null || commande.getNomCommande().isBlank()) {
			throw new DonneesInvalidesException("Le nom est obligatoire");
		}
		if (commande.getQuantite() != null && commande.getQuantite() <= 0) {
			throw new DonneesInvalidesException("La quantité doit être supérieure ou égale à 1");
		}
		if (commande.getCategorieSalarie() == null || commande.getCategorieSalarie().getId() == null) {
			throw new DonneesInvalidesException("La catégorie est obligatoire");
		}
	}

}