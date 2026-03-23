package com.easybee.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybee.backend.entity.DetailProduit;
import com.easybee.backend.repository.DetailProduitRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/details-commandes")
@RequiredArgsConstructor
public class DetailProduitController {

	private final DetailProduitRepository detailProduitRepository;

	@GetMapping("/{commandeId}")
	public List<DetailProduit> getDetails(@PathVariable Long commandeId) {
		return detailProduitRepository.findByIdIdCmdeApproDepot(commandeId);
	}
}