package com.xebia.solutions.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.xebia.solutions.dto.CompletedVaccinationDTO;
import com.xebia.solutions.dto.PageResponse;
import com.xebia.solutions.dto.ScheduledVaccinationDTO;
import com.xebia.solutions.service.ReportingService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ReportingController.class)
public class ReportingControllerTest {

	private static final LocalDate START_DATE = LocalDate.now();
	private static final LocalDate END_DATE = LocalDate.now().plusDays(1);

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ReportingService service;

	private PageResponse<ScheduledVaccinationDTO> scheduledVaccinationResponse = new PageResponse<>();

	private PageResponse<CompletedVaccinationDTO> completedVaccinationResponse = new PageResponse<>();

	@Test
	public void testGetAppointmentsForCentre() throws Exception {
		given(service.getAppointmentsForCentre(1, 0, 10)).willReturn(scheduledVaccinationResponse);
		mvc.perform(get("/v1/vaccinations/1/scheduled").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void testGetAllAppointmentsForPeriod() throws Exception {
		given(service.getAppointmentsForPeriod(START_DATE, END_DATE, 0, 10))
				.willReturn(scheduledVaccinationResponse);
		mvc.perform(get("/v1/vaccinations/scheduled?startDate={startDate}&endDate={endDate}", START_DATE, END_DATE))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetCompletedAppointments() throws Exception {
		given(service.getCompletedVaccinationsForPeriod(START_DATE, END_DATE, 0, 10))
				.willReturn(completedVaccinationResponse);
		mvc.perform(get("/v1/vaccinations/confirmed?startDate={startDate}&endDate={endDate}", START_DATE, END_DATE))
				.andExpect(status().isOk());
	}
}