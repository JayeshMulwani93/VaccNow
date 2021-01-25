package com.xebia.solutions.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.xebia.solutions.model.Appointment;
import com.xebia.solutions.model.VaccinationCentre;
import com.xebia.solutions.model.Vaccine;
import com.xebia.solutions.repository.AppointmentRepository;
import com.xebia.solutions.service.NotificationService;
import com.xebia.solutions.service.VaccinationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VaccinationServiceImpl implements VaccinationService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private NotificationService notificationService;

	@Override
	@Scheduled(fixedRate = 15 * 60 * 1000) // 15mins
	public void vaccinate() {		
		log.debug("Performing vaccinations now!");

		// Get All appointments on today which have not been completed and current time is after the start time
		List<Appointment> appointments = appointmentRepository.getCurrentAppointments(LocalDate.now(), LocalTime.now());

		appointments.stream().forEach(appointment -> {
			log.debug("Vaccinating user {}", appointment.getUser().getName());

			// Get vaccination centre and get any available vaccine from the centre's inventory. 
			VaccinationCentre vaccinationCentre = appointment.getSlot().getCentre();
			Vaccine vaccine = vaccinationCentre.getAnyAvailableVaccine();

			// update the appointment so that it can be used for tracking results in future.
			appointment.setVaccine(vaccine);
			appointment.setVaccinated(true);
			appointmentRepository.save(appointment);

			// Send out a mail with details like user + center + date
			notificationService.sendVaccinationCertificate(appointment.getUser(), vaccinationCentre,
					appointment.getSlot().getDate());
		});
	}
}