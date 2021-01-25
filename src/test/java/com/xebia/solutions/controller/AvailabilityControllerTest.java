package com.xebia.solutions.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.xebia.solutions.dto.AvailabilityDetails;
import com.xebia.solutions.dto.AvailableTimeSlots;
import com.xebia.solutions.dto.PageResponse;
import com.xebia.solutions.dto.TimeSlotStatusDTO;
import com.xebia.solutions.dto.VaccinationCentreDTO;
import com.xebia.solutions.dto.VaccineDTO;
import com.xebia.solutions.service.TimeSlotService;
import com.xebia.solutions.service.VaccinationCentreService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AvailabilityController.class)
public class AvailabilityControllerTest {

	private static final LocalTime LOCAL_TIME = LocalTime.now();
	private static final LocalDate LOCAL_DATE = LocalDate.now();

	@Autowired
	private MockMvc mvc;

	@MockBean
	private VaccinationCentreService vaccinationCentreService;

	@MockBean
	private TimeSlotService timeSlotService;

	private PageResponse<VaccinationCentreDTO> vaccinationCentresPage = new PageResponse<>();

	private PageResponse<VaccineDTO> vaccinesPage = new PageResponse<>();

	@Test
	public void testGetAllVaccinationCentres() throws Exception {
		given(vaccinationCentreService.getAllVaccinationCentres(0, 10)).willReturn(vaccinationCentresPage);
		mvc.perform(get("/v1/vaccinationcentres").contentType("application/json")).andExpect(status().isOk());
	}

	@Test
	public void testGetAvailableVaccines() throws Exception {
		given(vaccinationCentreService.getAllVaccinesForCentre(1, 0, 10)).willReturn(vaccinesPage);
		mvc.perform(get("/v1/vaccinationcentres/1/vaccines").contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void testIsAvailableTimeSlot() throws Exception {
		given(timeSlotService.isAvailable(1, LOCAL_DATE, LOCAL_TIME))
				.willReturn(new AvailabilityDetails(TimeSlotStatusDTO.AVAILABLE));
		mvc.perform(get("/v1/vaccinationcentres/1/availability?date={localDate}&hour={hour}&minute={minute}",
				LOCAL_DATE, LOCAL_TIME.getHour(), LOCAL_TIME.getMinute()).contentType("application/json"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetAvailableTimeSlots() throws Exception {
		given(timeSlotService.getAvailableTimeSlots(1, LOCAL_DATE)).willReturn(new AvailableTimeSlots());
		mvc.perform(get("/v1/vaccinationcentres/1/availability/all?date={localDate}", LOCAL_DATE)
				.contentType("application/json")).andExpect(status().isOk());
	}
}