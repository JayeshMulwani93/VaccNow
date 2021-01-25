package com.xebia.solutions.service;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xebia.solutions.model.Appointment;
import com.xebia.solutions.model.TimeSlot;
import com.xebia.solutions.model.User;
import com.xebia.solutions.model.VaccinationCentre;
import com.xebia.solutions.model.Vaccine;
import com.xebia.solutions.repository.AppointmentRepository;
import com.xebia.solutions.service.impl.VaccinationServiceImpl;

@ExtendWith(SpringExtension.class)
public class VaccinationServiceTest {

	private static final LocalDate DATE = LocalDate.now();
	private static final LocalTime START_TIME = LocalTime.now();
	private static final LocalTime END_TIME = LocalTime.now().plusMinutes(15);

	@Spy
	@InjectMocks
	private VaccinationService vaccinationService = new VaccinationServiceImpl();

	@Mock
	private AppointmentRepository appointmentRepository;

	@Mock
	private NotificationService notificationService;

	private List<Appointment> appointments;

	@BeforeEach
	public void setUp() {
		VaccinationCentre centre1 = new VaccinationCentre();
		centre1.setName("Test Centre");
		centre1.setId(1);
		
		Vaccine vaccine = new Vaccine(1, "A", "B");
		List<Vaccine> vaccines = new ArrayList<Vaccine>();
		vaccines.add(vaccine);

		VaccinationCentre centre2 = new VaccinationCentre();
		centre2.setName("Test Centre");
		centre2.setId(2);
		centre2.setVaccines(vaccines);
		centre1.setVaccines(vaccines);

		TimeSlot ts1 = new TimeSlot(DATE, START_TIME, END_TIME, centre1, 5);
		TimeSlot ts2 = new TimeSlot(DATE, START_TIME, END_TIME, centre2, 2);

		User user1 = new User("A", "A@gmail.com");
		User user2 = new User("B", "B@gmail.com");

		appointments = new ArrayList<>();
		appointments.add(new Appointment(user1, "CASH", ts1));
		appointments.add(new Appointment(user2, "CASH", ts2));

		when(appointmentRepository.getCurrentAppointments(ArgumentMatchers.any(LocalDate.class),
				ArgumentMatchers.any(LocalTime.class))).thenReturn(appointments);
	}

	@Test
	public void testVaccinate() {
		vaccinationService.vaccinate();

		for (Appointment app : appointments) {
			Assertions.assertTrue(app.isVaccinated());
		}

		Mockito.verify(appointmentRepository, Mockito.times(appointments.size()))
				.save(ArgumentMatchers.any(Appointment.class));

		Mockito.verify(notificationService, Mockito.times(appointments.size())).sendVaccinationCertificate(
				ArgumentMatchers.any(User.class), ArgumentMatchers.any(VaccinationCentre.class),
				ArgumentMatchers.any(LocalDate.class));
	}

}
