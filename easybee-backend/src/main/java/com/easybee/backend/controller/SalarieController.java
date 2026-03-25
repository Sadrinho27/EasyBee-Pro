package com.easybee.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybee.backend.entity.Salarie;
import com.easybee.backend.service.SalarieService;

@RestController
@RequestMapping("/api/salaries")
public class SalarieController {

	private final SalarieService salarieService;

	public SalarieController(SalarieService salarieService) {
		this.salarieService = salarieService;
	}

	@PostMapping
	public Salarie ajouterSalarie(@RequestBody Salarie nouveauSalarie) {
		return salarieService.createSalarie(nouveauSalarie);
	}

	@PutMapping("/{id}")
	public Salarie modifierSalarie(@PathVariable Long id, @RequestBody Salarie salarieDetails) {
		return salarieService.updateSalarie(id, salarieDetails);
	}

	@DeleteMapping("/{id}")
	public void supprimerSalarie(@PathVariable Long id) {
		salarieService.deleteSalarie(id);
	}

	@GetMapping
	public List<Salarie> getAllSalarie() {
		return salarieService.getAll();
	}

}