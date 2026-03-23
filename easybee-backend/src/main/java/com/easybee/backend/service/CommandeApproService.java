package com.easybee.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easybee.backend.entity.CommandeAppro;
import com.easybee.backend.repository.CommandeApproRepository;
import com.easybee.backend.repository.ProduitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandeApproService {

	private final CommandeApproRepository commandeApproRepository;
	private final ProduitRepository produitRepo;
	
	public List<CommandeAppro> getAll() {
		return commandeApproRepository.findAll();
	}

	public CommandeAppro validerCommande(Long id) {
		// 1. On cherche la commande. Si elle n'existe pas, on envoie une erreur propre.
		CommandeAppro commande = commandeApproRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Commande introuvable avec l'id : " + id));

		// 2. On change le statut (on passe de "en attente" à "livrée")
		commande.setStatutCommande("livrée");

		// 3. On sauvegarde la modification en base
		return commandeApproRepository.save(commande);
	}
	
	public CommandeAppro sauvegarder(CommandeAppro commande) {
	    return commandeApproRepository.save(commande);
	}
}