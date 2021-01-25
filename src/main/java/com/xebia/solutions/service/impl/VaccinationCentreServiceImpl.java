package com.xebia.solutions.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.xebia.solutions.dto.PageResponse;
import com.xebia.solutions.dto.VaccinationCentreDTO;
import com.xebia.solutions.dto.VaccineDTO;
import com.xebia.solutions.exceptions.VaccinationCentreNotFound;
import com.xebia.solutions.model.VaccinationCentre;
import com.xebia.solutions.model.Vaccine;
import com.xebia.solutions.repository.VaccinationCentreRepository;
import com.xebia.solutions.repository.VaccineRepository;
import com.xebia.solutions.service.VaccinationCentreService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VaccinationCentreServiceImpl implements VaccinationCentreService {

	@Autowired
	private VaccinationCentreRepository centreRepository;

	@Autowired
	private VaccineRepository vaccineRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PageResponse<VaccinationCentreDTO> getAllVaccinationCentres(int pageNumber, int pageSize) {
		log.debug("Retrieving all the vaccination Centres");
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
		Page<VaccinationCentre> vaccinationCentres = centreRepository.findAll(pageRequest);

		List<VaccinationCentreDTO> vaccinationCentreDtos = vaccinationCentres.stream()
				.map(centre -> modelMapper.map(centre, VaccinationCentreDTO.class))
				.collect(Collectors.toList());

		return new PageResponse<VaccinationCentreDTO>(vaccinationCentreDtos, pageNumber, pageSize,
				vaccinationCentres.getTotalPages(), vaccinationCentres.getTotalElements());
	}

	@Override
	public PageResponse<VaccineDTO> getAllVaccinesForCentre(int id, int pageNumber, int pageSize) {
		log.debug("Retrieving all the vaccines for Centre {}", id);
		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

		centreRepository.findById(id).orElseThrow(() -> new VaccinationCentreNotFound());

		Page<Vaccine> vaccinesPage = vaccineRepository.findByCentresId(id, pageRequest);
		List<VaccineDTO> vaccines = vaccinesPage.getContent().stream()
				.map(vaccine -> modelMapper.map(vaccine, VaccineDTO.class))
				.collect(Collectors.toList());

		return new PageResponse<VaccineDTO>(vaccines, pageNumber, pageSize, vaccinesPage.getTotalPages(),
				vaccinesPage.getTotalElements());
	}
}
