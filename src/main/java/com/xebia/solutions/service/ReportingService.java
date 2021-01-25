package com.xebia.solutions.service;

import java.time.LocalDate;

import com.xebia.solutions.dto.CompletedVaccinationDTO;
import com.xebia.solutions.dto.PageResponse;
import com.xebia.solutions.dto.ScheduledVaccinationDTO;

public interface ReportingService {

	public PageResponse<ScheduledVaccinationDTO> getAppointmentsForCentre(int centreId, int pageNumber, int pageSize);

	PageResponse<ScheduledVaccinationDTO> getAppointmentsForPeriod(LocalDate startDate, LocalDate endDate,
			int pageNumber, int pageSize);

	PageResponse<CompletedVaccinationDTO> getCompletedVaccinationsForPeriod(LocalDate startDate, LocalDate endDate,
			int pageNumber, int pageSize);

}
