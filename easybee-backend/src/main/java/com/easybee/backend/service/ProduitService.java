package com.easybee.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easybee.backend.entity.Produit;
import com.easybee.backend.exception.ProduitIntrouvableException;
import com.easybee.backend.repository.ProduitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProduitService {

	private final ProduitRepository produitRepository;

	public Produit createProduit(Produit nouveauProduit) {
		this.verifierProduit(nouveauProduit);
		nouveauProduit.setId(null);

		return produitRepository.save(nouveauProduit);
	}

	public Produit updateProduit(Long id, Produit produitDetails) {
		return produitRepository.findById(id).map(produitExistant -> {
			this.verifierProduit(produitDetails);

			produitExistant.setCodeProduit(produitDetails.getCodeProduit());
			produitExistant.setStockMagasin(produitDetails.getStockMagasin());
			produitExistant.setStockMinimum(produitDetails.getStockMinimum());
			produitExistant.setDesignation(produitDetails.getDesignation());
			produitExistant.setPrix(produitDetails.getPrix());
			produitExistant.setStockEntrepot(produitDetails.getStockEntrepot());

			return produitRepository.save(produitExistant);

		}).orElseThrow(() -> new ProduitIntrouvableException(id));
	}

	public void deleteProduit(Long id) {
		if (!produitRepository.existsById(id)) {
			throw new ProduitIntrouvableException(id);
		}

		produitRepository.deleteById(id);
	}

	public List<Produit> getAll() {
		return produitRepository.findAll();
	}

	public Produit getById(Long id) {
		return produitRepository.findById(id).orElseThrow(() -> new ProduitIntrouvableException(id));
	}

	public List<Produit> getProduitsAlerteStock() {
		return produitRepository.getProduitsAlerteStock();
	}

	private void verifierProduit(Produit produit) {
		if (produit.getDesignation() == null || produit.getDesignation().isBlank()) {
			throw new IllegalArgumentException("La désignation est obligatoire");
		}

		if (produit.getPrix() == null || produit.getPrix() <= 0) {
			throw new IllegalArgumentException("Le prix doit être supérieur à 0");
		}
	}

}