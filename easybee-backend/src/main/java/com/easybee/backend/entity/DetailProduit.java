package com.easybee.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detailproduit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailProduit {

	@EmbeddedId
	private DetailProduitId id;

	@Column(name = "qteCmde")
	private int quantiteCommandee;

	@ManyToOne
	@MapsId("idProduit") // Lie l'idProduit de la clé composée à l'objet Produit
	@JoinColumn(name = "idProduit")
	private Produit produit;

	@ManyToOne
	@MapsId("idCmdeApproDepot") // Lie l'idCmdeApproDepot à l'objet Commande
	@JoinColumn(name = "idCmdeApproDepot")
	private CommandeAppro commande;
}