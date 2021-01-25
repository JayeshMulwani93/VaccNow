package com.xebia.solutions.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xebia.solutions.model.TimeSlot;
import com.xebia.solutions.model.User;
import com.xebia.solutions.model.VaccinationCentre;
import com.xebia.solutions.service.EmailService;
import com.xebia.solutions.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	public static final String SCHEDULE_TIMESLOT_CONTENT = "Your Appointment has been setup at "
			+ "Vaccination Centre %s for Date %s at time %s";

	public static final String VACCINATION_COMPLETION_CONTENT = "Hi %s have successfully completed "
			+ "your covid-19 vaccination from  %s on %s";

	@Autowired
	private EmailService emailService;

	@Override
	public void sendTimeSlotConfirmation(User user, TimeSlot timeSlot) {
		String content = String.format(SCHEDULE_TIMESLOT_CONTENT, timeSlot.getCentre().getName(), timeSlot.getDate(),
				timeSlot.getStartTime());
		emailService.sendEmail(user.getEmailId(), content);
	}

	@Override
	public void sendVaccinationCertificate(User user, VaccinationCentre vaccinationCentre, LocalDate date) {
		String content = String.format(VACCINATION_COMPLETION_CONTENT, user.getName(), vaccinationCentre.getName(), date);
		emailService.sendEmail(user.getEmailId(), content);
	}
}
