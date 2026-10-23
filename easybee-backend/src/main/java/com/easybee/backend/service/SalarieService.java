package com.easybee.backend.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.easybee.backend.entity.Salarie;
import com.easybee.backend.exception.ConflitException;
import com.easybee.backend.exception.DonneesInvalidesException;
import com.easybee.backend.exception.RessourceIntrouvableException;
import com.easybee.backend.repository.SalarieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalarieService {

	private static final String RESSOURCE = "Salarié";
	private final SalarieRepository salarieRepository;

	public Salarie createSalarie(Salarie nouveauSalarie) {
		this.verifierSalarie(nouveauSalarie);

		if (salarieRepository.findByIdentifiant(nouveauSalarie.getIdentifiant()).isPresent()) {
			throw new ConflitException("Cet identifiant est déjà utilisé");
		}

		nouveauSalarie.setId(null);

		String motDePasse = nouveauSalarie.getMotDePasse();
		if (motDePasse == null || motDePasse.isBlank()) {
			throw new DonneesInvalidesException("Le mot de passe est obligatoire");
		}

		String mdpHasher = this.hasher(motDePasse);
		nouveauSalarie.setMotDePasse(mdpHasher);

		return salarieRepository.save(nouveauSalarie);
	}

	public Salarie updateSalarie(Long id, Salarie salarieDetails) {
		return salarieRepository.findById(id).map(salarieExistant -> {
			this.verifierSalarie(salarieDetails);
			if (salarieRepository.findByIdentifiant(salarieDetails.getIdentifiant())
					.filter(autre -> !autre.getId().equals(salarieExistant.getId())).isPresent()) {
				throw new ConflitException("Cet identifiant est déjà utilisé");
			}

			salarieExistant.setMatriculeSalarie(salarieDetails.getMatriculeSalarie());
			salarieExistant.setNomSalarie(salarieDetails.getNomSalarie());
			salarieExistant.setPrenomSalarie(salarieDetails.getPrenomSalarie());
			salarieExistant.setIdentifiant(salarieDetails.getIdentifiant());
			if (salarieDetails.getMotDePasse() != null && !salarieDetails.getMotDePasse().isBlank()) {
				salarieExistant.setMotDePasse(this.hasher(salarieDetails.getMotDePasse()));
			}
			salarieExistant.setCategorie(salarieDetails.getCategorie());

			return salarieRepository.save(salarieExistant);

		}).orElseThrow(() -> new RessourceIntrouvableException(RESSOURCE, id));
	}

	public void deleteSalarie(Long id) {
		if (!salarieRepository.existsById(id)) {
			throw new RessourceIntrouvableException(RESSOURCE, id);
		}

		salarieRepository.deleteById(id);
	}

	public List<Salarie> getAll() {
		return salarieRepository.findAll();
	}

	public Salarie getById(Long id) {
		return salarieRepository.findById(id).orElseThrow(() -> new RessourceIntrouvableException(RESSOURCE, id));
	}

	private void verifierSalarie(Salarie salarie) {
		if (salarie.getMatriculeSalarie() == null || salarie.getMatriculeSalarie() <= 0) {
			throw new DonneesInvalidesException("Le matricule est obligatoire");
		}
		if (salarie.getNomSalarie() == null || salarie.getNomSalarie().isBlank()) {
			throw new DonneesInvalidesException("Le nom est obligatoire");
		}
		if (salarie.getPrenomSalarie() == null || salarie.getPrenomSalarie().isBlank()) {
			throw new DonneesInvalidesException("Le prénom est obligatoire");
		}
		if (salarie.getIdentifiant() == null || salarie.getIdentifiant().isBlank()) {
			throw new DonneesInvalidesException("L'identifiant est obligatoire");
		}
		if (salarie.getCategorie() == null || salarie.getCategorie().getId() == null) {
			throw new DonneesInvalidesException("La catégorie est obligatoire");
		}
	}

	private String hasher(String mdp) {
		String salt = BCrypt.gensalt(12);
		return BCrypt.hashpw(mdp, salt);
	}
}