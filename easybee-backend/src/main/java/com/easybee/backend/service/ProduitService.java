package com.easybee.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easybee.backend.entity.Produit;
import com.easybee.backend.repository.ProduitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProduitService {

	private final ProduitRepository produitRepository;

	public Produit createProduit(Produit nouveauProduit) {
		nouveauProduit.setId(null);

		return produitRepository.save(nouveauProduit);
	}

	public Produit updateProduit(Long id, Produit produitDetails) {
		return produitRepository.findById(id).map(produitExistant -> {

			produitExistant.setCodeProduit(produitDetails.getCodeProduit());
			produitExistant.setStockMagasin(produitDetails.getStockMagasin());
			produitExistant.setStockMinimum(produitDetails.getStockMinimum());
			produitExistant.setDesignation(produitDetails.getDesignation());
			produitExistant.setPrix(produitDetails.getPrix());
			produitExistant.setStockEntrepot(produitDetails.getStockEntrepot());

			return produitRepository.save(produitExistant);

		}).orElseThrow(() -> new RuntimeException("Erreur : Produit introuvable avec l'ID " + id));
	}

	public void deleteProduit(Long id) {
		if (!produitRepository.existsById(id)) {
			throw new RuntimeException("Erreur : Impossible de supprimer, produit introuvable avec l'ID " + id);
		}

		produitRepository.deleteById(id);
	}

	public List<Produit> getAll() {
		return produitRepository.findAll();
	}
}