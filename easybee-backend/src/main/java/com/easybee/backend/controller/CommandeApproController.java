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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.easybee.backend.entity.CommandeAppro;
import com.easybee.backend.service.CommandeApproService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeApproController {

	private final CommandeApproService commandeApproService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CommandeAppro creerCommande(@RequestBody CommandeAppro nouvelleCommande) {
		return commandeApproService.createCommande(nouvelleCommande);
	}
	
	@GetMapping
	public List<CommandeAppro> getAll() {
		return commandeApproService.getAll();
	}

	@PutMapping("/{id}/valider")
	public CommandeAppro valider(@PathVariable Long id) {
		return commandeApproService.validerCommande(id);
	}
	
	@GetMapping("/{id}")
	public CommandeAppro getCommande(@PathVariable Long id) {
		return commandeApproService.getById(id);
	}

}