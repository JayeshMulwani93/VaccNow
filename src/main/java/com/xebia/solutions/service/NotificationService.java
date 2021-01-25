package com.xebia.solutions.service;

import java.time.LocalDate;

import com.xebia.solutions.model.TimeSlot;
import com.xebia.solutions.model.User;
import com.xebia.solutions.model.VaccinationCentre;

public interface NotificationService {

	public void sendTimeSlotConfirmation(User user, TimeSlot timeSlot);
	
	public void sendVaccinationCertificate(User user, VaccinationCentre vaccinationCentre, LocalDate date);

}
