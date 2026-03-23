package com.easybee.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "salarie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salarie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer matriculeSalarie;
	private String nomSalarie;
	private String prenomSalarie;
	private String identifiant;

	@JsonIgnore // Empêche le mot de passe d'être inclus dans le JSON sortant
	@Column(name = "motDePasse", nullable = false)
	private String motDePasse;

	@ManyToOne
	@JoinColumn(name = "idCat") // Nom de la FK dans ton SQL
	private CategorieSalarie categorie;
}