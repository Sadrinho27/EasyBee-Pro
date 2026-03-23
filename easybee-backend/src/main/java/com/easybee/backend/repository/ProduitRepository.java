package com.easybee.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybee.backend.entity.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

}