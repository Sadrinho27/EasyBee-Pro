package com.easybee.backend.entity;

import java.time.LocalDateTime;

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
@Table(name = "cmdeapprodepot")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeAppro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDateTime dateCommande;

	private String statutCommande;

	private String nomCommande;

	private Integer quantite;

	@ManyToOne
	@JoinColumn(name = "idCatSalarie") // La clé étrangère dans ton SQL
	private CategorieSalarie categorieSalarie;
}