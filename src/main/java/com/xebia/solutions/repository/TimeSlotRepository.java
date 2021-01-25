package com.xebia.solutions.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.xebia.solutions.model.TimeSlot;

@Repository
public interface TimeSlotRepository extends CrudRepository<TimeSlot, Long> {

	TimeSlot findTimeSlotByCentreIdAndDateAndStartTime(Integer centreId, LocalDate date, LocalTime time);
	
	@Query(value = "select * from TIME_SLOTS ts where ts.centre_id = ?1 and ts.date = ?2 and ts.capacity = ?3", nativeQuery = true)
	List<TimeSlot> findTimeSlotsAtMaxCapacity(Integer centreId, LocalDate date, long capacity);

}