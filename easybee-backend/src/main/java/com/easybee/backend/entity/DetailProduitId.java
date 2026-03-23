package com.easybee.backend.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailProduitId implements Serializable {
	private Long idProduit;
	private Long idCmdeApproDepot;
}