package com.xebia.solutions.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xebia.solutions.dto.CompletedVaccinationDTO;
import com.xebia.solutions.dto.PageResponse;
import com.xebia.solutions.dto.ScheduledVaccinationDTO;
import com.xebia.solutions.service.ReportingService;

@RestController
@RequestMapping(path = "/v1/vaccinations", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportingController {

	@Autowired
	private ReportingService reportingService;

	@GetMapping("/{centreId}/scheduled")
	public ResponseEntity<PageResponse<ScheduledVaccinationDTO>> getAppointmentsForBranch(
			@PathVariable int centreId,
			@RequestParam(required = false, defaultValue = "0") int pageNumber,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {

		return ResponseEntity.ok(reportingService.getAppointmentsForCentre(centreId, pageNumber, pageSize));
	}

	@GetMapping("/scheduled")
	public ResponseEntity<PageResponse<ScheduledVaccinationDTO>> getAppointmentsForPeriod(
			@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false, defaultValue = "0") int pageNumber,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {

		return ResponseEntity
				.ok(reportingService.getAppointmentsForPeriod(startDate, endDate, pageNumber, pageSize));
	}

	@GetMapping("/confirmed")
	public ResponseEntity<PageResponse<CompletedVaccinationDTO>> getCompletedAppointments(
			@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false, defaultValue = "0") int pageNumber,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {

		return ResponseEntity
				.ok(reportingService.getCompletedVaccinationsForPeriod(startDate, endDate, pageNumber, pageSize));
	}
}