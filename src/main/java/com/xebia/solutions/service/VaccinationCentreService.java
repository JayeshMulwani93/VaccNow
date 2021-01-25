package com.xebia.solutions.service;

import com.xebia.solutions.dto.PageResponse;
import com.xebia.solutions.dto.VaccinationCentreDTO;
import com.xebia.solutions.dto.VaccineDTO;

public interface VaccinationCentreService {

	PageResponse<VaccinationCentreDTO> getAllVaccinationCentres(int pageNumber, int pageSize);

	PageResponse<VaccineDTO> getAllVaccinesForCentre(int id, int pageNumber, int pageSize);
}
