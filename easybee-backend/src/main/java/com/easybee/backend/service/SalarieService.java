package com.easybee.backend.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.easybee.backend.entity.Salarie;
import com.easybee.backend.repository.SalarieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalarieService {

	private final SalarieRepository salarieRepository;

	public Salarie createSalarie(Salarie nouveauSalarie) {
	    nouveauSalarie.setId(null);

	    String motDePasse = nouveauSalarie.getMotDePasse();
	    if (motDePasse == null || motDePasse.isBlank()) {
	        throw new IllegalArgumentException("Mot de passe invalide");
	    }

	    String mdpHasher = BCrypt.hashpw(motDePasse, BCrypt.gensalt(12));
	    nouveauSalarie.setMotDePasse(mdpHasher);

	    return salarieRepository.save(nouveauSalarie);
	}

	public Salarie updateSalarie(Long id, Salarie salarieDetails) {
		return salarieRepository.findById(id).map(salarieExistant -> {

			salarieExistant.setMatriculeSalarie(salarieDetails.getMatriculeSalarie());
			salarieExistant.setNomSalarie(salarieDetails.getNomSalarie());
			salarieExistant.setPrenomSalarie(salarieDetails.getPrenomSalarie());
			salarieExistant.setIdentifiant(salarieDetails.getIdentifiant());
			salarieExistant.setMotDePasse(salarieDetails.getMotDePasse());
			salarieExistant.setCategorie(salarieDetails.getCategorie());

			return salarieRepository.save(salarieExistant);

		}).orElseThrow(() -> new RuntimeException("Erreur : Salarie introuvable avec l'ID " + id));
	}

	public void deleteSalarie(Long id) {
		if (!salarieRepository.existsById(id)) {
			throw new RuntimeException("Erreur : Impossible de supprimer, salarie introuvable avec l'ID " + id);
		}

		salarieRepository.deleteById(id);
	}

	public List<Salarie> getAll() {
		return salarieRepository.findAll();
	}
}