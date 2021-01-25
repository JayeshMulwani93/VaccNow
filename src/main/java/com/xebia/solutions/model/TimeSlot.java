package com.xebia.solutions.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "TIME_SLOTS")
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private LocalDate date;

	private LocalTime startTime;

	private LocalTime endTime;

	@ManyToOne
	private VaccinationCentre centre;
	
	@OneToMany(mappedBy = "slot", cascade = CascadeType.ALL)
	private List<Appointment> appointments;

    @Column(columnDefinition = "bigint default 0")
	private long capacity;

	public void incrementCapacity() {
		this.capacity++;
	}

	public TimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime, VaccinationCentre centre, long capacity) {
		super();
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.centre = centre;
		this.capacity = capacity;
	}

}