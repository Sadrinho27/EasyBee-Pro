package com.easybee.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easybee.backend.entity.Produit;
import com.easybee.backend.exception.DonneesInvalidesException;
import com.easybee.backend.exception.RessourceIntrouvableException;
import com.easybee.backend.repository.ProduitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProduitService {

	private static final String RESSOURCE = "Produit";
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

		}).orElseThrow(() -> new RessourceIntrouvableException(RESSOURCE, id));
	}

	public void deleteProduit(Long id) {
		if (!produitRepository.existsById(id)) {
			throw new RessourceIntrouvableException(RESSOURCE, id);
		}

		produitRepository.deleteById(id);
	}

	public List<Produit> getAll() {
		return produitRepository.findAll();
	}

	public Produit getById(Long id) {
		return produitRepository.findById(id).orElseThrow(() -> new RessourceIntrouvableException(RESSOURCE, id));
	}

	public List<Produit> getProduitsAlerteStock() {
		return produitRepository.getProduitsAlerteStock();
	}

	private void verifierProduit(Produit produit) {
		if (produit.getCodeProduit() == null) {
			throw new DonneesInvalidesException("Le code produit est obligatoire");
		}
		if (produit.getDesignation() == null || produit.getDesignation().isBlank()) {
			throw new DonneesInvalidesException("La désignation est obligatoire");
		}
		if (produit.getPrix() == null || produit.getPrix() <= 0) {
			throw new DonneesInvalidesException("Le prix doit être supérieur à 0");
		}
		this.verifierStock(produit.getStockMagasin(), "Le stock magasin");
		this.verifierStock(produit.getStockMinimum(), "Le stock minimum");
		this.verifierStock(produit.getStockEntrepot(), "Le stock entrepot");
	}

	// Un stock est obligatoire, peut valoir 0 (rupture), mais jamais être négatif
	private void verifierStock(Integer stock, String libelle) {
		if (stock == null) {
			throw new DonneesInvalidesException(libelle + " est obligatoire");
		}
		if (stock < 0) {
			throw new DonneesInvalidesException(libelle + " ne peut pas être négatif");
		}
	}

}