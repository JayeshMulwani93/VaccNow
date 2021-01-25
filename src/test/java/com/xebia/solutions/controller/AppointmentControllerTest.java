package com.xebia.solutions.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.xebia.solutions.dto.ScheduleAppointmentRequest;
import com.xebia.solutions.exceptions.UnavailableTimeSlotException;
import com.xebia.solutions.service.TimeSlotService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AppointmentController.class)
public class AppointmentControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TimeSlotService service;

	@Test
	public void testScheduleAppointment() throws Exception {
		mvc.perform(post("/v1/appointments").content(getContent()).contentType("application/json"))
				.andExpect(status().isCreated());
	}

	@Test
	public void testScheduleAppointmentFailure() throws Exception {
		BDDMockito.doThrow(UnavailableTimeSlotException.class).when(service)
				.scheduleAppointment(ArgumentMatchers.any(ScheduleAppointmentRequest.class));
		mvc.perform(post("/v1/appointments").content(getContent())
				.contentType("application/json"))
			.andExpect(status().isConflict());
	}
	
	private String getContent() {
		return "{\n"
				+ "  \"userId\": 1,\n"
				+ "  \"centreId\": 1,\n"
				+ "  \"date\": \"2021-10-25\",\n"
				+ "  \"hour\": 0,\n"
				+ "  \"minute\": 0,\n"
				+ "  \"paymentMethod\": \"string\"\n"
				+ "}";
	}
}