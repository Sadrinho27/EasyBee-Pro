package com.easybee.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybee.backend.entity.Produit;
import com.easybee.backend.service.ProduitService;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "http://localhost:3000") // Important pour ton futur React !
public class ProduitController {

	private final ProduitService produitService;

	public ProduitController(ProduitService produitService) {
		this.produitService = produitService;
	}

	@GetMapping
	public List<Produit> getAllProduits() {
		return produitService.getAll();
	}

}