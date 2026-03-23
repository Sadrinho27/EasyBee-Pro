package com.easybee.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybee.backend.entity.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

	Optional<Produit> findByDesignation(String designation);

}