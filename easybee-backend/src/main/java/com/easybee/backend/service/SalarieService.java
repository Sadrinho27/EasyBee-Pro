package com.easybee.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.easybee.backend.entity.Salarie;
import com.easybee.backend.repository.SalarieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SalarieService {

	private final SalarieRepository salarieRepository;

	public List<Salarie> getAll() {
		return salarieRepository.findAll();
	}
}