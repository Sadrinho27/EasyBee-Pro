package com.easybee.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.easybee.backend.entity.Produit;
import com.easybee.backend.service.ProduitService;

@RestController
@RequestMapping("/api/produits")
public class ProduitController {

	private final ProduitService produitService;

	public ProduitController(ProduitService produitService) {
		this.produitService = produitService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Produit ajouterProduit(@RequestBody Produit nouveauProduit) {
		return produitService.createProduit(nouveauProduit);
	}

	@PutMapping("/{id}")
	public Produit modifierProduit(@PathVariable Long id, @RequestBody Produit produitDetails) {
		return produitService.updateProduit(id, produitDetails);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void supprimerProduit(@PathVariable Long id) {
		produitService.deleteProduit(id);
	}
	
	@GetMapping
	public List<Produit> getAllProduits() {
		return produitService.getAll();
	}
	
	@GetMapping("/{id}")
	public Produit getProduit(@PathVariable Long id) {
		return produitService.getById(id);
	}
	
	@GetMapping("/alerte-stock")
	public List<Produit> getProduitsAlerteStock() {
		return produitService.getProduitsAlerteStock();
	}
}