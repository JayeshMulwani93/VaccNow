package com.xebia.solutions.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xebia.solutions.model.Appointment;
import com.xebia.solutions.model.AppointmentId;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, AppointmentId> {

	@Query(value = "select * from Appointments app , time_slots ts where app.slot_id = ts.id"
			+ " and ts.date = ?1 and app.is_vaccinated = 0 and ts.start_time <= ?2", nativeQuery = true)
	List<Appointment> getCurrentAppointments(LocalDate date, LocalTime time);

	@Query(value = "select * from Appointments app , time_slots ts where app.slot_id = ts.id"
			+ " and app.is_vaccinated = 0 and ts.centre_id = ?1 "
			+ " order by date, start_time", nativeQuery = true)
	Page<Appointment> getAppointmentsForCentre(int centreId, Pageable pageable);

	@Query(value = "select * from Appointments app , time_slots ts where app.slot_id = ts.id"
			+ " and app.is_vaccinated = 0 and ts.date >= ?1 and ts.date <= ?2"
			+ " order by date, start_time", nativeQuery = true)
	Page<Appointment> getAppointmentsForPeriod(LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	@Query(value = "select * from Appointments app , time_slots ts where app.slot_id = ts.id"
			+ " and app.is_vaccinated = 1 and ts.date >= ?1 and ts.date <= ?2"
			+ " order by date, start_time", nativeQuery = true)
	Page<Appointment> getCompletedVaccinationsForPeriod(LocalDate startDate, LocalDate endDate, Pageable pageable);

}