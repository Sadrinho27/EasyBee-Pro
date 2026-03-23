package com.easybee.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybee.backend.entity.CommandeAppro;
import com.easybee.backend.service.CommandeApproService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeApproController {

	private final CommandeApproService commandeApproService;

	@GetMapping
	public List<CommandeAppro> getAll() {
		return commandeApproService.getAll();
	}

	@PutMapping("/{id}/valider")
	public ResponseEntity<CommandeAppro> valider(@PathVariable Long id) {
		try {
			CommandeAppro commandeModifiee = commandeApproService.validerCommande(id);
			return ResponseEntity.ok(commandeModifiee);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping
	public ResponseEntity<CommandeAppro> creerCommande(@RequestBody CommandeAppro nouvelleCommande) {
		// Par défaut, une nouvelle commande est toujours "en attente"
		nouvelleCommande.setStatutCommande("en attente");
		// On peut aussi forcer la date à "maintenant" si ton entité ne le fait pas
		// nouvelleCommande.setDateCommande(LocalDateTime.now());

		CommandeAppro creee = commandeApproService.sauvegarder(nouvelleCommande);
		return ResponseEntity.status(HttpStatus.CREATED).body(creee);
	}
}