package com.xebia.solutions.service;

import java.time.LocalDate;
import java.time.LocalTime;

import com.xebia.solutions.dto.AvailabilityDetails;
import com.xebia.solutions.dto.AvailableTimeSlots;
import com.xebia.solutions.dto.ScheduleAppointmentRequest;

public interface TimeSlotService {

	void scheduleAppointment(ScheduleAppointmentRequest appointmentRequest);

	AvailabilityDetails isAvailable(int centreId, LocalDate date, LocalTime time);

	AvailableTimeSlots getAvailableTimeSlots(int centreId, LocalDate date);
}