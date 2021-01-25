package com.xebia.solutions.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xebia.solutions.model.TimeSlot;
import com.xebia.solutions.model.VaccinationCentre;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TimeSlotRepositoryTest {
	private static final LocalDate LOCAL_DATE = LocalDate.now();
	private static final LocalTime START_TIME = LocalTime.now();
	private static final LocalTime END_TIME = LocalTime.now().plusMinutes(15);
	private static Integer centreId;

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private TimeSlotRepository repository;

	@BeforeEach
	public void setUp() {

		VaccinationCentre centre = new VaccinationCentre();
		centre.setName("Test Centre");

		centre = testEntityManager.persist(centre);
		centreId = centre.getId();

		TimeSlot timeSlot1 = new TimeSlot(LOCAL_DATE, START_TIME, END_TIME, centre, 5);
		TimeSlot timeSlot2 = new TimeSlot(LOCAL_DATE, START_TIME.plusHours(1), END_TIME.plusHours(1), centre, 2);
		TimeSlot timeSlot3 = new TimeSlot(LOCAL_DATE, START_TIME.plusHours(2), END_TIME.plusHours(2), centre, 5);

		testEntityManager.persist(timeSlot1);
		testEntityManager.persist(timeSlot2);
		testEntityManager.persist(timeSlot3);
	}

	@Test
	public void testFindTimeSlotsAtMaxCapacity() {
		List<TimeSlot> timeSlotsAtMaxCapacity = repository.findTimeSlotsAtMaxCapacity(centreId, LOCAL_DATE, 5);
		Assertions.assertEquals(2, timeSlotsAtMaxCapacity.size());
	}

	@Test
	public void testFindTimeSlotByCentreIdAndDateAndStartTimeSuccess() {
		TimeSlot response = repository.findTimeSlotByCentreIdAndDateAndStartTime(centreId, LOCAL_DATE, START_TIME);
		Assertions.assertNotNull(response);
	}

	@Test
	public void testFindTimeSlotByCentreIdAndDateAndStartTimeInvalidCentreId() {
		TimeSlot response = repository.findTimeSlotByCentreIdAndDateAndStartTime(2, LOCAL_DATE, START_TIME);
		Assertions.assertNull(response);
	}
}
