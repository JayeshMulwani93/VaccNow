package com.xebia.solutions.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xebia.solutions.model.TimeSlot;
import com.xebia.solutions.model.User;
import com.xebia.solutions.model.VaccinationCentre;
import com.xebia.solutions.service.impl.NotificationServiceImpl;

@ExtendWith(SpringExtension.class)
public class NotificationServiceTest {

	@Spy
	@InjectMocks
	private NotificationService service = new NotificationServiceImpl();

	@Mock
	private EmailService emailService;

	private User user;

	private VaccinationCentre centre;

	private TimeSlot ts;

	@BeforeEach
	public void setUp() {
		user = new User("A", "A@gmail.com");
		centre = new VaccinationCentre();
		centre.setName("A");
		ts = new TimeSlot(LocalDate.now(), LocalTime.now(), LocalTime.now().plusMinutes(15), new VaccinationCentre(), 0);
	}

	@Test
	public void testSendTimeSlotConfirmation() {
		service.sendTimeSlotConfirmation(user, ts);
		Mockito.verify(emailService).sendEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
	}

	@Test
	public void testSendVaccinationCertificate() {
		service.sendVaccinationCertificate(user, centre, LocalDate.now());
		Mockito.verify(emailService).sendEmail(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
	}

}
