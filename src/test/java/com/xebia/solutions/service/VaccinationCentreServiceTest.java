package com.xebia.solutions.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.xebia.solutions.dto.PageResponse;
import com.xebia.solutions.dto.VaccinationCentreDTO;
import com.xebia.solutions.dto.VaccineDTO;
import com.xebia.solutions.model.VaccinationCentre;
import com.xebia.solutions.model.Vaccine;
import com.xebia.solutions.repository.VaccinationCentreRepository;
import com.xebia.solutions.repository.VaccineRepository;
import com.xebia.solutions.service.impl.VaccinationCentreServiceImpl;

@ExtendWith(SpringExtension.class)
public class VaccinationCentreServiceTest {

	private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

	@InjectMocks
	private VaccinationCentreService service = new VaccinationCentreServiceImpl();

	@Mock
	private VaccinationCentreRepository vaccinationCentreRepository;

	@Mock
	private VaccineRepository vaccineRepository;

	@Mock
	private ModelMapper modelMapper;

	private List<VaccinationCentre> vaccinationCentres;
	private List<VaccinationCentreDTO> vaccinationCentresDtos;
	private List<Vaccine> vaccines;
	private List<VaccineDTO> vaccineDtos;

	@BeforeEach
	public void setUp() {
		VaccinationCentre centre = new VaccinationCentre();
		centre.setName("Test Centre");
		centre.setId(1);
		centre.setCapacity(10);

		VaccinationCentreDTO centreDTO = new VaccinationCentreDTO();
		centreDTO.setName("Test Centre");
		centreDTO.setId(1);
		centreDTO.setCapacity(10);

		vaccinationCentres = new ArrayList<>();
		vaccinationCentres.add(centre);

		vaccinationCentresDtos = new ArrayList<>();
		vaccinationCentresDtos.add(centreDTO);

		vaccines = new ArrayList<>();
		vaccines.add(new Vaccine(1, "A", "B"));
		vaccines.add(new Vaccine(2, "C", "D"));

		vaccineDtos = new ArrayList<>();
		vaccineDtos.add(new VaccineDTO(1, "A", "B"));
		vaccineDtos.add(new VaccineDTO(1, "A", "B"));
	}

	@Test
	public void testGetAllVaccinationCentres() {
		when(vaccinationCentreRepository.findAll(PAGE_REQUEST)).thenReturn(new PageImpl<>(vaccinationCentres));
		when(modelMapper.map(any(), eq(VaccinationCentreDTO.class))).thenReturn(spy(VaccinationCentreDTO.class));
		PageResponse<VaccinationCentreDTO> actualResponse = service.getAllVaccinationCentres(0, 10);

		assertNotNull(actualResponse);
		verify(modelMapper, atLeastOnce()).map(any(), eq(VaccinationCentreDTO.class));
		verify(vaccinationCentreRepository, times(1)).findAll(PAGE_REQUEST);
	}

	@Test
	public void testGetAllVaccinesForCentre() {
		when(vaccinationCentreRepository.findById(1)).thenReturn(Optional.of(vaccinationCentres.get(0)));
		when(vaccineRepository.findByCentresId(1, PAGE_REQUEST)).thenReturn(new PageImpl<>(vaccines));
		when(modelMapper.map(any(), eq(VaccineDTO.class))).thenReturn(spy(VaccineDTO.class));
		PageResponse<VaccineDTO> actualResponse = service.getAllVaccinesForCentre(1, 0, 10);

		assertNotNull(actualResponse);
	}
}
