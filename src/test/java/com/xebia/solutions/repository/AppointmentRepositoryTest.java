package com.xebia.solutions.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xebia.solutions.model.Appointment;
import com.xebia.solutions.model.TimeSlot;
import com.xebia.solutions.model.User;
import com.xebia.solutions.model.VaccinationCentre;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AppointmentRepositoryTest {

	private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);
	private static final LocalDate DATE = LocalDate.now();
	private static final LocalTime START_TIME = LocalTime.now();
	private static final LocalTime END_TIME = LocalTime.now().plusMinutes(15);
	
	private static Integer centreId1, centreId2;

	@Autowired
	private TestEntityManager entityMgr;

	@Autowired
	private AppointmentRepository repository;

	private List<Appointment> appointments;

	@BeforeEach
	public void setUp() {

		VaccinationCentre centre1 = new VaccinationCentre();
		centre1.setName("Test Centre");

		centre1 = entityMgr.persist(centre1);
		centreId1 = centre1.getId();

		VaccinationCentre centre2 = new VaccinationCentre();
		centre2.setName("Test Centre");

		centre2 = entityMgr.persist(centre2);
		centreId2 = centre2.getId();

		TimeSlot ts1 = entityMgr.persist(new TimeSlot(DATE, START_TIME, END_TIME, centre1, 5));
		TimeSlot ts2 = entityMgr.persist(new TimeSlot(DATE, START_TIME.minusHours(1), END_TIME.minusHours(1), centre1, 2));
		TimeSlot ts3 = entityMgr.persist(new TimeSlot(DATE, START_TIME.minusHours(2), END_TIME.minusHours(2), centre1, 2));
		TimeSlot ts4 = entityMgr.persist(new TimeSlot(DATE, START_TIME, END_TIME, centre2, 2));

		User user1 = entityMgr.persist(new User("A", "A@gmail.com"));
		User user2 = entityMgr.persist(new User("B", "B@gmail.com"));
		User user3 = entityMgr.persist(new User("C", "C@gmail.com"));
		User user4 = entityMgr.persist(new User("D", "D@gmail.com"));

		appointments = new ArrayList<>();
		appointments.add(entityMgr.persist(new Appointment(user1, "CASH", ts1)));
		appointments.add(entityMgr.persist(new Appointment(user2, "CASH", ts2)));
		appointments.add(entityMgr.persist(new Appointment(user3, "CASH", ts3)));
		appointments.add(entityMgr.persist(new Appointment(user4, "FAWRY", ts4)));
	}

	@Test
	public void testGetCurrentAppointments() {
		List<Appointment> currentAppointments = repository.getCurrentAppointments(DATE, LocalTime.now());
		Assertions.assertEquals(appointments, currentAppointments);
	}

	@Test
	public void testGetAppointmentsForCentre1() {
		Page<Appointment> currentAppointments = repository.getAppointmentsForCentre(centreId1, PAGE_REQUEST);
		Assertions.assertEquals(3, currentAppointments.getNumberOfElements());
	}

	@Test
	public void testGetAppointmentsForCentre2() {
		Page<Appointment> currentAppointments = repository.getAppointmentsForCentre(centreId2, PAGE_REQUEST);
		Assertions.assertEquals(1, currentAppointments.getNumberOfElements());
	}
	
	@Test
	public void testGetAppointmentsForPeriodToday() {
		LocalDate date = LocalDate.now();
		Page<Appointment> response = repository.getAppointmentsForPeriod(date, date, PAGE_REQUEST);
		Assertions.assertTrue(appointments.containsAll(response.getContent()));
	}
	
	@Test
	public void testGetAppointmentsForInvalidPeriod() {
		LocalDate date = LocalDate.now();
		Page<Appointment> response = repository.getAppointmentsForPeriod(date.plusDays(1), date.plusDays(1), PAGE_REQUEST);
		Assertions.assertTrue(response.getContent().size() == 0);
	}
	
	@Test
	public void testGetCompletedVaccinations() {
		LocalDate date = LocalDate.now();
		
		appointments.forEach((app) -> app.setVaccinated(true));
		Page<Appointment> response = repository.getCompletedVaccinationsForPeriod(date, date, PAGE_REQUEST);
		Assertions.assertTrue(appointments.containsAll(response.getContent()));
	}

	@Test
	public void testNoneCompletedVaccinations() {
		LocalDate date = LocalDate.now();
		Page<Appointment> response = repository.getCompletedVaccinationsForPeriod(date, date, PAGE_REQUEST);
		Assertions.assertTrue(response.getContent().size() == 0);
	}
}
