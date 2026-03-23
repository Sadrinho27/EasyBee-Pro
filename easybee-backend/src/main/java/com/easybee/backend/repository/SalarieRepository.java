package com.easybee.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easybee.backend.entity.Salarie;

public interface SalarieRepository extends JpaRepository<Salarie, Long> {
	// Indispensable pour la future page de connexion
	Optional<Salarie> findByIdentifiant(String identifiant);
}