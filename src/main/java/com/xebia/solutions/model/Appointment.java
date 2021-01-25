package com.xebia.solutions.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "APPOINTMENTS")
@NoArgsConstructor
public class Appointment {

	@EmbeddedId
	private AppointmentId id = new AppointmentId();

	@ManyToOne
	@MapsId("userId")
	private User user;

	@ManyToOne
	@MapsId("slotId")
	private TimeSlot slot;

	private String paymentMethod;

	@ManyToOne
	private Vaccine vaccine;

	@Column(columnDefinition = "boolean default false")
	private boolean isVaccinated;

	public Appointment(User user, String paymentMethod, TimeSlot slot) {
		this.user = user;
		this.paymentMethod = paymentMethod;
		this.slot = slot;
	}
}