package com.easybee.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.easybee.backend.entity.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

	Optional<Produit> findByDesignation(String designation);

	@Query("SELECT x FROM Produit x WHERE x.stockMagasin < x.stockMinimum")
	List<Produit> getProduitsAlerteStock();
}