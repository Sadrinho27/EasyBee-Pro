package com.easybee.backend.repository;

import com.easybee.backend.entity.DetailProduit;
import com.easybee.backend.entity.DetailProduitId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetailProduitRepository extends JpaRepository<DetailProduit, DetailProduitId> {

	// Spring Data JPA comprend tout seul qu'il doit chercher par l'ID de la
	// commande
	// qui se trouve à l'intérieur de l'objet "id" (DetailProduitId)
	List<DetailProduit> findByIdIdCmdeApproDepot(Long idCommande);
}	