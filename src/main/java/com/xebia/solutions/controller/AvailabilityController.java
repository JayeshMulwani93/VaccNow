package com.xebia.solutions.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.Valid;
import javax.validation.constraints.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xebia.solutions.dto.AvailabilityDetails;
import com.xebia.solutions.dto.AvailableTimeSlots;
import com.xebia.solutions.dto.PageResponse;
import com.xebia.solutions.dto.VaccinationCentreDTO;
import com.xebia.solutions.dto.VaccineDTO;
import com.xebia.solutions.service.TimeSlotService;
import com.xebia.solutions.service.VaccinationCentreService;

@RestController
@RequestMapping(path = "/v1/vaccinationcentres", produces = MediaType.APPLICATION_JSON_VALUE)
public class AvailabilityController {

	@Autowired
	private VaccinationCentreService vaccinationCentreService;

	@Autowired
	private TimeSlotService timeSlotService;

	@GetMapping("/{centreId}/vaccines")
	public ResponseEntity<PageResponse<VaccineDTO>> getAvailableVaccines(@PathVariable Integer centreId,
			@RequestParam(required = false, defaultValue = "0") int pageNumber,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {

		return ResponseEntity.ok(vaccinationCentreService.getAllVaccinesForCentre(centreId, pageNumber, pageSize));
	}

	@GetMapping
	public ResponseEntity<PageResponse<VaccinationCentreDTO>> getAllCentres(
			@RequestParam(required = false, defaultValue = "0") int pageNumber,
			@RequestParam(required = false, defaultValue = "10") int pageSize) {

		return ResponseEntity.ok(vaccinationCentreService.getAllVaccinationCentres(pageNumber, pageSize));
	}

	@GetMapping("/{centreId}/availability")
	public ResponseEntity<AvailabilityDetails> isAvailable(@PathVariable int centreId,
			@Valid @Future @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam(required = true) int hour, @RequestParam(required = true) int minute) {

		return ResponseEntity.ok(timeSlotService.isAvailable(centreId, date, LocalTime.of(hour, minute)));
	}

	@GetMapping("/{centreId}/availability/all")
	public ResponseEntity<AvailableTimeSlots> getAvailableTimeSlots(@PathVariable int centreId,
			@Valid @Future @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return ResponseEntity.ok(timeSlotService.getAvailableTimeSlots(centreId, date));
	}
}