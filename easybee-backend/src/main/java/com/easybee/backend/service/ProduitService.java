package com.easybee.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.easybee.backend.entity.Produit;
import com.easybee.backend.repository.ProduitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProduitService {

	private final ProduitRepository produitRepository;

	public List<Produit> getAll() {
		return produitRepository.findAll();
	}

	// Une méthode "Métier" : récupérer uniquement les produits en alerte stock
	public List<Produit> getProduitsEnAlerte() {
		return produitRepository.findAll().stream().filter(p -> p.getStockMagasin() < p.getStockMinimum())
				.collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
					if (list.isEmpty())
						System.out.println("Aucune alerte, les abeilles dorment tranquilles !");
					return list;
				}));
	}
}