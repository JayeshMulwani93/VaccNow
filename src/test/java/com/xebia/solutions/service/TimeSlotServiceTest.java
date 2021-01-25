package com.xebia.solutions.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xebia.solutions.dto.ScheduleAppointmentRequest;
import com.xebia.solutions.dto.TimeSlotStatusDTO;
import com.xebia.solutions.exceptions.UnavailableTimeSlotException;
import com.xebia.solutions.model.Appointment;
import com.xebia.solutions.model.TimeSlot;
import com.xebia.solutions.model.User;
import com.xebia.solutions.model.VaccinationCentre;
import com.xebia.solutions.repository.AppointmentRepository;
import com.xebia.solutions.repository.TimeSlotRepository;
import com.xebia.solutions.repository.UserRepository;
import com.xebia.solutions.repository.VaccinationCentreRepository;
import com.xebia.solutions.service.impl.TimeSlotServiceImpl;

@ExtendWith(SpringExtension.class)
public class TimeSlotServiceTest {

	private static final int CAPACITY = 2;

	private static final int CENTRE_ID = 1;

	private static final LocalTime TIME = LocalTime.of(10, 0);

	private static final LocalDate DATE = LocalDate.now();

	@InjectMocks
	private TimeSlotServiceImpl service;

	@Mock
	private UserRepository userRepository;

	@Mock
	private VaccinationCentreRepository vaccinationCentreRepository;

	@Mock
	private AppointmentRepository appointmentRepository;

	@Mock
	private NotificationService notificationService;

	@Mock
	private TimeSlotRepository timeSlotRepository;

	private List<LocalTime> allTimeSlots;

	private VaccinationCentre centre;

	private ScheduleAppointmentRequest appointmentRequest;

	@BeforeEach
	public void setUp() {
		service.setPaymentMethods(new ArrayList<>(Arrays.asList("CASH", "FAWRY")));
		service.setTimeSlots(new ArrayList<>(Arrays.asList(0, 15, 30, 45)));
		centre = new VaccinationCentre();
		centre.setId(CENTRE_ID);
		centre.setCapacity(CAPACITY);
		centre.setName("A");

		appointmentRequest = new ScheduleAppointmentRequest();
		appointmentRequest.setCentreId(CENTRE_ID);
		appointmentRequest.setDate(DATE);
		appointmentRequest.setHour(TIME.getHour());
		appointmentRequest.setMinute(TIME.getMinute());
		appointmentRequest.setPaymentMethod("CASH");
		appointmentRequest.setUserId(1);

		when(userRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(new User()));

		when(vaccinationCentreRepository.findById(CENTRE_ID)).thenReturn(Optional.of(centre));

		allTimeSlots = new ArrayList<LocalTime>();
		allTimeSlots.add(TIME);
		allTimeSlots.add(TIME.plusMinutes(15));
		allTimeSlots.add(TIME.plusMinutes(30));
		service.setAllTimeSlots(allTimeSlots);
	}

	@Test
	public void testAvailableScenarioTimeSlotDoesNotExist() {
		when(timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(eq(centre.getId()), eq(DATE), eq(TIME)))
				.thenReturn(null);
		assertEquals(TimeSlotStatusDTO.AVAILABLE, service.isAvailable(CENTRE_ID, DATE, TIME).getStatus());
	}

	@Test
	public void testAvailableScenarioTimeSlotExists() {
		when(timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(eq(centre.getId()), eq(DATE), eq(TIME)))
				.thenReturn(new TimeSlot(DATE, TIME, TIME.plusMinutes(15), centre, 1));
		assertEquals(TimeSlotStatusDTO.AVAILABLE, service.isAvailable(CENTRE_ID, DATE, TIME).getStatus());
	}

	@Test
	public void testUnAvailableTimeSlot() {
		when(timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(eq(centre.getId()), eq(DATE), eq(TIME)))
				.thenReturn(new TimeSlot(DATE, TIME, TIME, centre, CAPACITY));
		assertEquals(TimeSlotStatusDTO.UNAVAILABLE, service.isAvailable(CENTRE_ID, DATE, TIME).getStatus());
	}

	@Test
	public void testScheduleAppointmentNoPriorTimeSlots() {
		when(timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(eq(centre.getId()), eq(DATE), eq(TIME)))
				.thenReturn(null);
		when(timeSlotRepository.save(any(TimeSlot.class))).thenReturn(new TimeSlot());

		service.scheduleAppointment(appointmentRequest);
		verify(timeSlotRepository, Mockito.times(1)).save(any(TimeSlot.class));
		verify(appointmentRepository, Mockito.times(1)).save(any(Appointment.class));
		verify(notificationService, Mockito.times(1)).sendTimeSlotConfirmation(any(User.class), any(TimeSlot.class));
	}

	@Test
	public void testScheduleAppointmentWhichIsAvailable() {
		TimeSlot timeSlot = new TimeSlot(DATE, TIME, TIME.plusMinutes(15), centre, 1);
		long initalCapacity = timeSlot.getCapacity();
		when(timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(eq(centre.getId()), any(LocalDate.class),
				any(LocalTime.class))).thenReturn(timeSlot);

		service.scheduleAppointment(appointmentRequest);

		assertEquals(initalCapacity + 1, timeSlot.getCapacity());
		verify(appointmentRepository, Mockito.times(1)).save(any(Appointment.class));
		verify(notificationService, Mockito.times(1)).sendTimeSlotConfirmation(any(User.class), any(TimeSlot.class));
	}

	@Test
	public void testScheduleUnavailableAppointment() {
		when(timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(eq(centre.getId()), any(LocalDate.class),
				any(LocalTime.class))).thenReturn(new TimeSlot(DATE, TIME, TIME, centre, CAPACITY));

		assertThrows(UnavailableTimeSlotException.class, () -> service.scheduleAppointment(appointmentRequest));
	}

	@Test
	public void testGetAllAvailableTimeSlots() {
		List<TimeSlot> timeSlots = new ArrayList<>();
		timeSlots.add(new TimeSlot(DATE, TIME, TIME, centre, CAPACITY));

		when(timeSlotRepository.findTimeSlotsAtMaxCapacity(ArgumentMatchers.anyInt(), any(LocalDate.class),
				ArgumentMatchers.anyLong())).thenReturn(timeSlots);

		assertEquals(allTimeSlots.size() - timeSlots.size(),
				service.getAvailableTimeSlots(CENTRE_ID, DATE).getData().size());
	}
}
