package com.easybee.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "codeProduit")
	private Integer codeProduit; // Remplacé int par Integer

	@Column(name = "stockMag")
	private Integer stockMagasin; // Remplacé int par Integer

	@Column(name = "stockMiniMag")
	private Integer stockMinimum; // Remplacé int par Integer

	@Column(name = "designationProduit")
	private String designation;

	@Column(name = "prixPdt")
	private Float prix; // Remplacé float par Float

	@Column(name = "stockEntrepot")
	private Integer stockEntrepot; // Remplacé int par Integer
}