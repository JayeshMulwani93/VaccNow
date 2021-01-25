package com.xebia.solutions.service.impl;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.xebia.solutions.dto.AvailabilityDetails;
import com.xebia.solutions.dto.AvailableTimeSlots;
import com.xebia.solutions.dto.ScheduleAppointmentRequest;
import com.xebia.solutions.dto.TimeSlotStatusDTO;
import com.xebia.solutions.exceptions.UnavailableTimeSlotException;
import com.xebia.solutions.exceptions.UnsupportedPaymentMethodException;
import com.xebia.solutions.exceptions.UnsupportedTimeSlotException;
import com.xebia.solutions.exceptions.UserNotFoundException;
import com.xebia.solutions.exceptions.VaccinationCentreNotFound;
import com.xebia.solutions.model.Appointment;
import com.xebia.solutions.model.TimeSlot;
import com.xebia.solutions.model.User;
import com.xebia.solutions.model.VaccinationCentre;
import com.xebia.solutions.repository.AppointmentRepository;
import com.xebia.solutions.repository.TimeSlotRepository;
import com.xebia.solutions.repository.UserRepository;
import com.xebia.solutions.repository.VaccinationCentreRepository;
import com.xebia.solutions.service.NotificationService;
import com.xebia.solutions.service.TimeSlotService;

import lombok.Setter;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {

	@Value("#{'${payment-methods}'.split(',')}")
	@Setter
	private List<String> paymentMethods;

	@Value("#{'${time-slots}'.split(',')}")
	@Setter
	private List<Integer> timeSlots;

	@Autowired
	private TimeSlotRepository timeSlotRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VaccinationCentreRepository vaccinationCentreRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	@Setter
	List<LocalTime> allTimeSlots;

	@Override
	public AvailabilityDetails isAvailable(int centreId, LocalDate date, LocalTime time) {
		VaccinationCentre centre = vaccinationCentreRepository.findById(centreId)
				.orElseThrow(() -> new VaccinationCentreNotFound());
		TimeSlot timeSlot = timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(centreId, date, time);

		long maxCapacity = centre.getCapacity();

		if (timeSlot == null || timeSlot.getCapacity() < maxCapacity) {
			return new AvailabilityDetails(TimeSlotStatusDTO.AVAILABLE);
		}

		return new AvailabilityDetails(TimeSlotStatusDTO.UNAVAILABLE);
	}

	@Override
	@Transactional
	public void scheduleAppointment(ScheduleAppointmentRequest appointmentRequest) {

		if (!isValid(appointmentRequest.getPaymentMethod())) {
			throw new UnsupportedPaymentMethodException();
		}

		if (!isValid(appointmentRequest.getMinute())) {
			throw new UnsupportedTimeSlotException();
		}

		int centreId = appointmentRequest.getCentreId();
		LocalDate date = appointmentRequest.getDate();
		LocalTime time = LocalTime.of(appointmentRequest.getHour(), appointmentRequest.getMinute());

		// Check if the slot requested is available!
		TimeSlotStatusDTO availability = isAvailable(centreId, date, time).getStatus();
		if (!TimeSlotStatusDTO.AVAILABLE.equals(availability))
			throw new UnavailableTimeSlotException();

		TimeSlot timeSlot = timeSlotRepository.findTimeSlotByCentreIdAndDateAndStartTime(centreId, date, time);

		Optional<User> userOptional = userRepository.findById(appointmentRequest.getUserId());
		User user = userOptional.orElseThrow(() -> new UserNotFoundException());

		Optional<VaccinationCentre> centreOpt = vaccinationCentreRepository.findById(centreId);
		VaccinationCentre vaccinationCentre = centreOpt.get();

		if (timeSlot == null) {
			timeSlot = new TimeSlot();
			timeSlot.setCentre(vaccinationCentre);
			timeSlot.setDate(date);
			timeSlot.setStartTime(time);
			timeSlot.setEndTime(timeSlot.getStartTime().plusMinutes(15));
			timeSlot = timeSlotRepository.save(timeSlot);
		}

		// If Available, Increase capacity/people present currently of the timeslot
		timeSlot.incrementCapacity();

		// Save the newly made appointment
		appointmentRepository.save(new Appointment(user, appointmentRequest.getPaymentMethod(), timeSlot));

		// Send Notification
		notificationService.sendTimeSlotConfirmation(user, timeSlot);

	}

	@Override
	public AvailableTimeSlots getAvailableTimeSlots(int centreId, LocalDate date) {
		Optional<VaccinationCentre> centreOpt = vaccinationCentreRepository.findById(centreId);
		VaccinationCentre centre = centreOpt.orElseThrow(() -> new VaccinationCentreNotFound());

		// The idea is to get all the slots based on the interval provided throughout
		// the day
		// Then check for timeslot which are at max capacity and remove them from the
		// overall timeSlots.
		List<LocalTime> timeSlots = new ArrayList<>();
		timeSlots.addAll(allTimeSlots);

		List<LocalTime> unavailableTimeSlots = timeSlotRepository
				.findTimeSlotsAtMaxCapacity(centreId, date, centre.getCapacity()).stream()
				.map(slot -> slot.getStartTime()).collect(toList());

		timeSlots.removeAll(unavailableTimeSlots);
		return new AvailableTimeSlots(timeSlots);
	}

	private boolean isValid(String paymentMethod) {
		return paymentMethods.contains(paymentMethod);
	}

	private boolean isValid(int minute) {
		return timeSlots.contains(minute);
	}
}
