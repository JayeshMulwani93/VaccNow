package com.xebia.solutions.dto;

import java.time.LocalDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ScheduleAppointmentRequest {

	@NotNull
	private long userId;

	@NotNull
	private int centreId;

	@NotNull
	@Future
	private LocalDate date;

	@NotNull
	@Min(value = 0)
	@Max(value = 23)
	private int hour;

	@NotNull
	@Min(value = 0)
	@Max(value = 45)
	private int minute;

	@NotNull
	private String paymentMethod;
}
