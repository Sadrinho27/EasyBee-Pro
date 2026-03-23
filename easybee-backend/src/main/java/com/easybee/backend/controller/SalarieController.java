package com.easybee.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybee.backend.entity.Salarie;
import com.easybee.backend.service.SalarieService;

@RestController
@RequestMapping("/api/salaries")
@CrossOrigin(origins = "http://localhost:3000") // Important pour ton futur React !
public class SalarieController {

	private final SalarieService salarieService;

	public SalarieController(SalarieService salarieService) {
		this.salarieService = salarieService;
	}

	@GetMapping("/tous")
	public List<Salarie> getAllProduits() {
		return salarieService.getAll();
	}

}