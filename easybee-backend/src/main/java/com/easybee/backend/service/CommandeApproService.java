package com.easybee.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easybee.backend.entity.CommandeAppro;
import com.easybee.backend.entity.Produit;
import com.easybee.backend.repository.CommandeApproRepository;
import com.easybee.backend.repository.ProduitRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandeApproService {

	private final CommandeApproRepository commandeApproRepository;
	private final ProduitRepository produitRepo;

	public List<CommandeAppro> getAll() {
		return commandeApproRepository.findAll();
	}

	@Transactional
	public CommandeAppro validerCommande(Long id) {
		// 1. Trouver la commande
		CommandeAppro commande = commandeApproRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Commande non trouvée"));

		if ("livrée".equals(commande.getStatutCommande())) {
			throw new RuntimeException("Cette commande est déjà validée !");
		}

		// 2. Trouver le produit correspondant
		Produit produit = produitRepo.findByDesignation(commande.getNomCommande())
				.orElseThrow(() -> new RuntimeException("Produit non trouvé en base"));

		// 3. Mise à jour du stock avec la VRAIE quantité
		int quantiteAAjouter = (commande.getQuantite() != null) ? commande.getQuantite() : 1;
		produit.setStockMagasin(produit.getStockMagasin() + quantiteAAjouter);
		produitRepo.save(produit);

		// 4. Changer le statut de la commande
		commande.setStatutCommande("livrée");
		return commandeApproRepository.save(commande);
	}

	public CommandeAppro sauvegarder(CommandeAppro commande) {
		return commandeApproRepository.save(commande);
	}
}