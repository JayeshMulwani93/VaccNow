package com.xebia.solutions.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ScheduledVaccinationDTO {

	private String userName;

	private String vaccinationCentre;

	private LocalDate date;

	@JsonFormat(pattern="HH-mm")
	private LocalTime time;
}